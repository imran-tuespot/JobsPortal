package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class AssignedPackageResponse {
	
	private Long id;
	private String assignDate;
	private String endDate;
	private Integer  days;
	private Integer  pendingDays;
	private boolean status;
	
}
