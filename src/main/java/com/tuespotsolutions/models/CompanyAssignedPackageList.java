package com.tuespotsolutions.models;

import java.util.Date;

import lombok.Data;

@Data
public class CompanyAssignedPackageList {

	private Long id;
	private String companyName;
	private String companyEmail;
	private String companyLocation;
	private String mobileNumber;
	private String packageName;
	private String packagePrice;
	private String packageAssignedDays;
	private String packagePendingDays;
	private String startDate;
	private String endDate;
	private boolean status;
	
}
