package com.tuespotsolutions.models;


import java.util.List;

import lombok.Data;

@Data
public class FiltersListForJobPost {

	private Long id;
	private String filterName;
	private String filterType;
	private boolean status;
	List<FilterValueResponseForPostJob> filterValues;
	
}
