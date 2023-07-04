package com.tuespotsolutions.service;


import java.util.List;

import com.tuespotsolutions.models.FilterValueResponse;
import com.tuespotsolutions.models.FilterValuesRequest;
import com.tuespotsolutions.models.FilteredJobsWithFilters;
import com.tuespotsolutions.models.FiltersList;
import com.tuespotsolutions.models.FiltersListForJobPost;
import com.tuespotsolutions.models.FiltersRequest;
import com.tuespotsolutions.models.FiltersRequestWithPagination;
import com.tuespotsolutions.models.JobFilterModel;
import com.tuespotsolutions.models.JobLocationsGroup;
import com.tuespotsolutions.models.JobResponseWithPagination;
import com.tuespotsolutions.models.JobWorkModeModel;
import com.tuespotsolutions.models.PostedOnJob;

public interface FilterService {

	
	public FiltersRequest addFilters(FiltersRequest filtersRequest);
	
	public FiltersRequest updateFilters(FiltersRequest filtersRequest);
	
	public FiltersRequestWithPagination getFilters(Integer page, Integer size);
	
    public FiltersRequest getFilterById(Long id);
    
    public List<FiltersRequest> getEnabledFilters();
    
    List<JobLocationsGroup> findJobLoactionGroup(String jobTitile);
    List<PostedOnJob> findPostedOnGroup(String jobTitle); 
    
    FilteredJobsWithFilters filterJobsByCity(List<JobLocationsGroup> cityId, Integer page, Integer size);
    
    FilteredJobsWithFilters filterJobsByPostedOn(List<PostedOnJob> postedOnJobs, Integer page, Integer size);
  
    public void deleteFilter(long id);
    
    public List<JobWorkModeModel> workModeFilterList(String jobTitle);
    
    public FilteredJobsWithFilters getJobWithWorkModeFilter(List<JobWorkModeModel> jobWorkModeModels, int page ,int size);
    	
    public List<FiltersListForJobPost>  findAllFilterListWithValues();
    
    public List<FiltersList> getFilterList(String jobTitle, String jobLocation);
    
    public JobResponseWithPagination getJobListByCheckedFilters(List<FiltersList> filtersList, int page ,int size);
    
}
