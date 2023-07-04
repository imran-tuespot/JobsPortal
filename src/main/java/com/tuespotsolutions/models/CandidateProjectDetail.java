package com.tuespotsolutions.models;


import java.util.Date;

import lombok.Data;

@Data
public class CandidateProjectDetail {

	private Long id;
	private String title;
	private String client;
	private boolean projectStatus;
	private Date projectStartDate;
	private Date projectEndDate;
	private String description;
	private String location;
	private String natureOfEmployement;
	private Integer teamSize;
	private String role;
	private String roleDescription;
	private String skillSet;
	private Long candidateId;
	
}
