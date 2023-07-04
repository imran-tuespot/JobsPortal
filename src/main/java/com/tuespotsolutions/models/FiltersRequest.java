package com.tuespotsolutions.models;

import java.util.Date;

import lombok.Data;

@Data
public class FiltersRequest {

	private Long id;
	private String filterName;
	private String filterType;
	private Date createdOn;
	private Date modifiedOn;
	private boolean status;
	
}
