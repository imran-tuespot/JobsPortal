package com.tuespotsolutions.models;

import java.util.Set;

import lombok.Data;

@Data
public class JobSearchFilters {

	private Set<DepartmentFilter> jobDepartment;
	private Set<WorkModeFilter> jobWorkMode;
	private Set<LocationFilters> jobLocation;
	private Set<JobCategoryFilters> jobCategory;
	
}
