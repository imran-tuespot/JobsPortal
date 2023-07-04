package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class FilterValueResponseForPostJob {
	
	private Long id;
	private String filterValue;
	private int jobCount;
	private boolean status;
	private String jobTitle;
}
