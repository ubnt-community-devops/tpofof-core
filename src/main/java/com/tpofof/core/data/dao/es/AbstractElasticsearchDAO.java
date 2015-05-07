package com.tpofof.core.data.dao.es;

import java.util.List;

import org.bson.types.ObjectId;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.data.dao.IPersistentModelDAO;
import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.utils.Config;
import com.tpofof.core.utils.json.JsonUtils;

/**
 * ALL implementing class should call {@link #init()} at the end of their constructor.
 * 
 * @author david
 *
 * @param <ModelT>
 */
@Component
public abstract class AbstractElasticsearchDAO<ModelT extends IPersistentModel<ModelT, String>> implements IPersistentModelDAO<ModelT, String, QueryBuilder> {

	@Autowired private JsonUtils json;
	private Client client;
	private Config config;
	
	public AbstractElasticsearchDAO(Config config, Client client) {
		this.config = config;
		this.client = client;
	}

	protected abstract String getIndex();
	
	protected abstract String getType();
	
	protected abstract Class<ModelT> getModelClass();
	
	protected abstract boolean isRequiredIndex();
	
	protected void init() {
		IndicesExistsResponse existsResult = client.admin().indices().prepareExists(getIndex()).execute().actionGet();
		if (!existsResult.isExists()) {
			CreateIndexRequestBuilder prepareCreateBuilder = client.admin().indices().prepareCreate(getIndex());
			if (hasMapping()) {
				prepareCreateBuilder.addMapping(getType(), getMapping());
			}
			CreateIndexResponse createResponse = prepareCreateBuilder.execute().actionGet();
			if ((createResponse == null || !createResponse.isAcknowledged()) && isRequiredIndex()) {
				throw new RuntimeException("Cannot create index " + getIndex());
			}
		}
	}
	
	/**
	 * If DAO does have a sort then the implementation should {@code override} {@link #getSort()}
	 * @return
	 */
	protected abstract boolean hasSort();
	
	protected SortBuilder getSort() {
		return null;
	}
	
	/**
	 * If DAO does have a custom mapping then the implementation should {@code override} {@link #getMapping()}
	 * @return
	 */
	protected abstract boolean hasMapping();
	
	protected String getMapping() {
		return null;
	}
	
	protected Client getClient() {
		return client;
	}
	
	protected Config getConfig() {
		return config;
	}
	
	protected JsonUtils getJsonUtils() {
		return json;
	}
	
	public ModelT  insert(ModelT model) {
		if (model.getId() == null || model.getId().isEmpty()) {
			model.setId(new ObjectId().toString());
		}
		String jsonSource = convert(model);
		IndexResponse response = getClient().prepareIndex()
				.setIndex(getIndex())
				.setType(getType())
				.setId(model.getId())
				.setSource(jsonSource)
				.execute()
				.actionGet();
		return model.getId().equals(response.getId()) ? model : null;
	}
	
	public ModelT find(String id) {
		GetResponse response = getClient().prepareGet()
				.setIndex(getIndex())
				.setType(getType())
				.setId(id)
				.execute()
				.actionGet();
		return convert(response.getSourceAsString());
	}
	
	public ResultsSet<ModelT> find(int limit, int offset) {
		return find(null, null, limit, offset);
	}
	
	/**
	 * 
	 * @param q Used for both findQuery and countQuery.
	 * @param limit
	 * @param offset
	 * @return
	 */
	public ResultsSet<ModelT> find(QueryBuilder q, int limit, int offset) {
		return find(q, q, limit, offset);
	}
	
	/**
	 * findQuery and countQuery should be the same for this Elasticsearch implementation.
	 */
	public ResultsSet<ModelT> find(QueryBuilder findQuery, QueryBuilder countQuery, int limit, int offset) {
		SearchRequestBuilder ps = getClient().prepareSearch()
				.setIndices(getIndex())
				.setTypes(getType());
		if (hasSort()) {
			ps.addSort(getSort());
		}
		if (findQuery != null) {
			ps.setQuery(findQuery);
		}
		SearchResponse response = ps.execute().actionGet();
		return convert(response, limit, offset, count(countQuery));
	}
	
	public long count() {
		CountResponse response = getClient().prepareCount()
				.setIndices(getIndex())
				.setTypes(getType())
				.execute()
				.actionGet();
		return response.getCount();
	}
	
	public long count(QueryBuilder query) {
		CountRequestBuilder request = getClient().prepareCount()
				.setIndices(getIndex())
				.setTypes(getType());
		if (query != null) {
			request = request.setQuery(query);
		}
		return request.execute()
				.actionGet()
				.getCount();
	}

	public ModelT update(ModelT model) {
		try {
			UpdateResponse results = getClient().prepareUpdate()
					.setIndex(getIndex())
					.setType(getType())
					.setId(model.getId())
					.setDoc(convert(model))
					.execute()
					.actionGet();
			return results != null && results.getId().equals(model.getId()) ? model : null;
		} catch (ElasticsearchException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean delete(String id) {
		DeleteResponse results = getClient().prepareDelete()
				.setIndex(getIndex())
				.setType(getType())
				.setId(id)
				.execute()
				.actionGet();
		return results.isFound();
	}
	
	protected ResultsSet<ModelT> convert(SearchResponse response, int limit, int offset, long total) {
		List<ModelT> models = Lists.newArrayList();
		for (SearchHit h : response.getHits()) {
			models.add(convert(h.sourceAsString()));
		}
		return ResultsSet.<ModelT>builder()
				.limit(limit)
				.offset(offset)
				.total(total)
				.results(models)
				.build();
	}
	
	protected String convert(ModelT model) {
		return json.toJson(model);
	}
	
	protected ModelT convert(String jsonContent) {
		return json.fromJson(jsonContent, getModelClass());
	}
}
