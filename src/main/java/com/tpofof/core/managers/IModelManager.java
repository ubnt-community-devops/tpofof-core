package com.tpofof.core.managers;

import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.data.dao.ResultsSet;


public interface IModelManager<ModelT extends IPersistentModel<ModelT, PrimaryKeyT>, PrimaryKeyT>  {

	public ModelT find(PrimaryKeyT id);
	public ResultsSet<ModelT> find();
	public ResultsSet<ModelT> find(int limit, int offset);
	public long count();
	public ModelT insert(ModelT model);
	public ModelT update(ModelT model);
	public boolean delete(PrimaryKeyT id);
}
