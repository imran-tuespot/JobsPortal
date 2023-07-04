package com.tuespotsolutions.models;

import java.util.Date;

import lombok.Data;

@Data
public class TrashCompanyInboxDetail {
	
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
