package com.tpofof.core.data;


public interface IPersistentModel<ModelT, PrimaryKeyT> {

	PrimaryKeyT getId();
	
	void setId(PrimaryKeyT id);
}
