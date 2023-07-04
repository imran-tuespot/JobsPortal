package com.tuespotsolutions.models;


import lombok.Data;

@Data
public class CandidateAssignedPackageList {
	private Long id;
	private String candidateName;
	private String canidateEmail;
	private String candidateLocation;
	private String mobileNumber;
	private String packageName;
	private String packagePrice;
	private String packageAssignedDays;
	private String packagePendingDays;
	private String startDate;
	private String endDate;
	private boolean status;
}
