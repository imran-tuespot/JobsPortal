package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class JobLocationsGroup {

	private Integer cityId;
	private String cityName;
	private String jobCount;
	private String jobTitle;
	private boolean status = false;
	
}
