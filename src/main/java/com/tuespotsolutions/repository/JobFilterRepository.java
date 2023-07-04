package com.tuespotsolutions.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.FilterValues;
import com.tuespotsolutions.entity.Filters;
import com.tuespotsolutions.entity.Job;
import com.tuespotsolutions.entity.JobFilter;

@Repository
public interface JobFilterRepository extends JpaRepository<JobFilter, Long>{

	
	public List<JobFilter> findByJob(Job job);
	
    public List<JobFilter> findByJobAndFilter(Job job, Filters filter);
    
    long countByFilterAndFilterValueAndJob(Filters filter, FilterValues filterValue, Job job);
    
    @Query(value = "SELECT count(job.id) as jobCount, jbf.filter as filterId , jbf.filter_value_id as filterValueId FROM jobfilter  jbf \r\n"
    		+ "				right join job job on  job.id = jbf.job_id where job.title like %:jobTitle% group by jbf.filter_value_id having jbf.filter_value_id =:filterValueId", nativeQuery = true)
    public FilterCountOfSeachedJob getCountBySearchedJob(@Param("jobTitle") String jobTitle, @Param("filterValueId") long filterValueId);
    
    
    public interface FilterCountOfSeachedJob{
    	public Long getJobCount();
    	public Long getFilterId();
    	public Long getFilterValueId();
    }
	
    
}
