package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.FilterValueResponse;
import com.tuespotsolutions.models.FilterValueResponseWithPagination;
import com.tuespotsolutions.models.FilterValuesRequest;

public interface FilterValueService {
	
	public List<FilterValueResponse> addFilterValue(List<FilterValuesRequest> filterValuesRequest);
	
	public FilterValueResponse updateFilterValue(FilterValuesRequest filterValuesRequest);
	
	public FilterValueResponseWithPagination getFilterList(int page, int size);
	
	public FilterValueResponse getFilterById(Long id);
	
	public void deleteFilterValue(Long id);
	
}
