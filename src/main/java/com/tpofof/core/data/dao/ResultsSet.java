package com.tpofof.core.data.dao;


import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultsSet<ModelT> {

	private int limit;
	private int offset;
	private List<ModelT> results;
	
}
