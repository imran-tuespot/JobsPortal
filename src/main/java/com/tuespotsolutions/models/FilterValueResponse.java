package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class FilterValueResponse {

	private Long id;
	private String filterValue;
	private FilterResponseForFilterValue filterResponse;
	
}
