package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class CandidateInboxReponseRecieved {
	
	private Long id;
	private String jobName;
	private String companyName;
	private String location;
	private String companyLogo;
	private String description;
	private String experience;
	private String activeHour;
	private String status;
	private String statusSeen;
}
