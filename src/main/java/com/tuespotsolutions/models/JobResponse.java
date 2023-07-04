package com.tuespotsolutions.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {

	private Long id;
	private String title;
	private String description;
	private String workMode;
	private Long workModeId;
	private String location;
	private String jobType;
	private String experience;
	private String skills;
	private String activeHours;
	private String department;
	private String logo;
	private boolean status;
	private Integer cityId;
	private CompanyResponse company;
	private List<FiltersListForJobPost> filterWithValues;
	
}
