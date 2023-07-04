package com.tuespotsolutions.models;

import java.util.List;

import lombok.Data;

@Data
public class PackagesResponseWithPagination {

	List<PackagesResponse> content;
	private int pageNumber;
	private int pageSize;
	private long totalElement;
	private int totalPages;
	private boolean lastPage;
	
}
