package com.tuespotsolutions.models;


import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Job {
	
	private Long id;
	
	@NotEmpty(message = "Job title is required field")
	private String title;
	private String description;
	private String type;
	@NotEmpty(message = "Job location is required field")
	private String location;
	@NotEmpty(message = "Job Type is required field")
	private String jobType;
	private Long workModeId;
	private String experience;
	@NotEmpty(message = "Job skills is required field")
	private String skills;
	@NotEmpty(message = "Department is required field")
	private String department;
	private boolean status;
	private Long companyId;
	private Integer cityId;
	
	private List<JobFilterWithValues> filterWithValues;
}
