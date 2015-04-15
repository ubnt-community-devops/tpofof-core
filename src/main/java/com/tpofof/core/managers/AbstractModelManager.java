package com.tpofof.core.managers;

import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.data.dao.IPersistentModelDAO;
import com.tpofof.core.data.dao.ResultsSet;

public abstract class AbstractModelManager<ModelT extends IPersistentModel<ModelT, PrimaryKeyT>, PrimaryKeyT, ModelDaoT extends IPersistentModelDAO<ModelT, PrimaryKeyT, QueryT>, QueryT> implements IModelManager<ModelT, PrimaryKeyT> {

	private final ModelDaoT dao;
	
	public AbstractModelManager(ModelDaoT dao) {
		this.dao = dao;
	}
	
	protected ModelDaoT getDao() {
		return dao;
	}
	
	public ModelT find(PrimaryKeyT id) {
		return dao.find(id);
	}
	
	public abstract int getDefualtLimit();

	public ResultsSet<ModelT> find() {
		return find(getDefualtLimit(), 0);
	}

	public ResultsSet<ModelT> find(int limit, int offset) {
		return dao.find(limit <= 0 ? getDefualtLimit() : limit, offset);
	}

	public long count() {
		return dao.count();
	}

	public ModelT insert(ModelT model) {
		return dao.insert(model);
	}
	
	public abstract PrimaryKeyT getDefaultId();

	public ModelT update(ModelT model) {
		if (model.getId() == null || model.getId().equals(getDefaultId())) {
			return this.insert(model);
		}
		return dao.update(model);
	}

	public boolean delete(PrimaryKeyT id) {
		return dao.delete(id);
	}

}
