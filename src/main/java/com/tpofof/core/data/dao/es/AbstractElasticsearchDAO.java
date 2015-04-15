package com.tpofof.core.data.dao.es;

import java.util.List;

import org.bson.types.ObjectId;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.data.dao.IPersistentModelDAO;
import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.utils.Config;
import com.tpofof.core.utils.json.JsonUtils;

@Component
public abstract class AbstractElasticsearchDAO<ModelT extends IPersistentModel<ModelT, String>> implements IPersistentModelDAO<ModelT, String, QueryBuilder> {

	@Autowired private JsonUtils json;
	@Autowired private Config config;
	private final Client client;
	private final Class<ModelT> modelClass;
	
	public AbstractElasticsearchDAO(Client client, Class<ModelT> modelClass) {
		this.client = client;
		this.modelClass = modelClass;
	}

	protected String getIndex() {
		return config.getString("es.index.name");
	}
	
	protected abstract String getType();
	
	public ModelT  insert(ModelT model) {
		if (model.getId() == null || model.getId().isEmpty()) {
			model.setId(new ObjectId().toString());
		}
		String jsonSource = convert(model);
		IndexResponse response = client.prepareIndex(getIndex(), getType(), model.getId())
			.setSource(jsonSource)
			.execute()
			.actionGet();
		return model.getId().equals(response.getId()) ? model : null;
	}
	
	public ModelT find(String id) {
		GetResponse response = client.prepareGet(getIndex(), getType(), id)
				.execute()
				.actionGet();
		return convert(response.toString());
	}
	
	public ResultsSet<ModelT> find(int limit, int offset) {
		return find(null, limit, offset);
	}
	
	public ResultsSet<ModelT> find(QueryBuilder q, int limit, int offset) {
		SearchRequestBuilder ps = client.prepareSearch(getIndex())
				.setTypes(getType());
		if (q != null) {
			ps.setQuery(q);
		}
		SearchResponse response = ps.execute().actionGet();
		List<ModelT> models = Lists.newArrayList();
		for (SearchHit h : response.getHits()) {
			models.add(convert(h.sourceAsString()));
		}
		return ResultsSet.<ModelT>builder()
				.limit(limit)
				.offset(offset)
				.results(models)
				.build();
	}
	
	public long count() {
		CountResponse response = client.prepareCount()
				.setIndices(getIndex())
				.setTypes(getType())
				.execute()
				.actionGet();
		return response.getCount();
	}

	/**
	 * NOT SUPPORTED
	 */
	@Deprecated
	public ModelT update(ModelT model) {
		return null;
	}

	/**
	 * NOT SUPPORTED
	 */
	@Deprecated
	public boolean delete(String id) {
		return false;
	}
	
	protected String convert(ModelT model) {
		return json.toJson(model);
	}
	
	protected ModelT convert(String jsonContent) {
		return json.fromJson(jsonContent, modelClass);
	}
}
