package com.tuespotsolutions.models;


import lombok.Data;

@Data
public class ShortListCompanyInboxDetail {

	private Long id;
	private String jobName;
	private String candidateName;
	private String location;
	private String activeHour;
	private String description;
	private String candidateImage;
	private String resume;
	private String status;
	private Long notifiationId;
	
}
