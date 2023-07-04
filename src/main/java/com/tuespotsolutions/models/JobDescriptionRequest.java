package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class JobDescriptionRequest {

	private long id;
	private String description;
	private long jobId;
	
}
