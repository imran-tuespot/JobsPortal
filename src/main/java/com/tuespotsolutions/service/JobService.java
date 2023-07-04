package com.tuespotsolutions.service;


import java.util.List;
import java.util.Set;

import com.tuespotsolutions.models.CityResponse;
import com.tuespotsolutions.models.Job;
import com.tuespotsolutions.models.JobResponse;
import com.tuespotsolutions.models.JobResponseWithPagination;
import com.tuespotsolutions.models.JobSearchFilters;
import com.tuespotsolutions.models.JobWorkModeModel;

public interface JobService {
	
	//save job
	public JobResponse saveJob(Job job);
	
	//save job
	public JobResponse updateJob(Job job);
	
	// get Job by company;
	public JobResponseWithPagination getJobsByComany(Integer page, Integer size, Long companyId);
	
	// get all jobs
	public JobResponseWithPagination getJobs(Integer page, Integer size);
	
	//get job by id
	public JobResponse getJobById(Long jobId);
	
	//delete job
	public void deleteJob(Long jobId);
	
	
	// search job by title and location
	public JobResponseWithPagination searchJobByTitleOrLocation(String jobTitle, String jobLocation, Integer page, Integer size);
	
	// job filters
	public JobSearchFilters jobSearchingFilters();
	
	// job search by filters
	public Set<JobResponse> findByFilters(JobSearchFilters jobSearchFilters);
	
	// find all cities 
	public List<CityResponse> findAllCities();
	
	// find job according to skills of candidate
	public JobResponseWithPagination findByJobAccordingToCandidateSkillSet(Long candidateId, Integer page, Integer size);
	
	// get Job by company;
	public JobResponseWithPagination getJobsByComanyForCompanyPanelJobList(Integer page, Integer size, Long companyId);

	
	
	
}
