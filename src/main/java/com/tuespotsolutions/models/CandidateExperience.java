package com.tuespotsolutions.models;

import java.sql.Date;

import lombok.Data;

@Data
public class CandidateExperience {

	private Long id;
	private boolean current;
	private String employmentType;
	private String companyName;
	private String designation;
	private Date joingDate;
	private Date endDate;
	private String ctc;
	private String jobProfile;
	private long candidateId;
	
}
