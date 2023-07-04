package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class CandidateSalaryDetail {
	
	private Long id;
	private String currentCtc;
	private String expectedCtc;
	private String noticePeriod;
	private Long canidateId;
	
}
