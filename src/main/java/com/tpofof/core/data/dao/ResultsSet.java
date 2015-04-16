package com.tpofof.core.data.dao;


import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ResultsSet<ModelT> {

	@NonNull private Integer limit;
	@NonNull private Integer offset;
	@NonNull private List<ModelT> results;
	
}
