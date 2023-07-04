package com.tuespotsolutions.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDiscriptionPagination {

	private List<PackageDiscriptionResponse> discriptionResponses;
	
	private int pageNumber;
	private int pageSize;
	private long totalElement;
	private int totalPages;
	private boolean lastPage;
}
