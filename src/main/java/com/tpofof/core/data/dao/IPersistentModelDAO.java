package com.tpofof.core.data.dao;

import com.tpofof.core.data.IPersistentModel;


public interface IPersistentModelDAO<ModelT extends IPersistentModel<ModelT, PrimaryKeyT>, PrimaryKeyT, QueryT> {

	public ResultsSet<ModelT> find(int limit, int offset);
	
	public long count();
	
	public long count(QueryT query);
	
	public ModelT find(PrimaryKeyT id);
	
	public ResultsSet<ModelT> find(QueryT query, int limit, int offset);
	
	public ModelT insert(ModelT model);
	
	public ModelT update(ModelT model);
	
	public boolean delete(PrimaryKeyT id);
}