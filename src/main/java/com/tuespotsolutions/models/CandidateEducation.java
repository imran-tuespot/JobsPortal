package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class CandidateEducation {
	
	private Long id;
	private String name;
	private String university;
	private String course;
	private String specialization;
	private String courseType;
	private String courseDuration;
	private String gradingSystem;
	private Double marks;
	private Long candidateId;
	
}
