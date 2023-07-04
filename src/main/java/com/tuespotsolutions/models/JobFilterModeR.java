package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class JobFilterModeR {
	
	private Long filterValueID;
	private String filterValue;
	private String jobTitle;
	private boolean status;
	private int count;

}
