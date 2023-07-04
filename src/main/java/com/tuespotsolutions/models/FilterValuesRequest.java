package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class FilterValuesRequest {
	
	private Long id;
	private String filterValue;
	private Long filterId;
	
}
