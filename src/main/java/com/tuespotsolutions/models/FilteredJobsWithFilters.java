package com.tuespotsolutions.models;

import java.util.List;

import lombok.Data;

@Data
public class FilteredJobsWithFilters {

	List<JobResponse> content;
	private int pageNumber;
	private int pageSize;
	private long totalElement;
	private int totalPages;
	private boolean lastPage;
	
}
