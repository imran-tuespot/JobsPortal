package com.tuespotsolutions.models;

import java.util.List;

import lombok.Data;

@Data
public class FiltersRequestWithPagination {

	private List<FiltersRequest> filtersRequests;
	private int pageNumber;
	private int pageSize;
	private long totalElement;
	private int totalPages;
	private boolean lastPage;
}
