package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class JobFilterModelValue {
	
	private long filterValueId;
	private String filterValues;
	private long jobCount;
	private boolean status;
	private String jobTitle;

}
