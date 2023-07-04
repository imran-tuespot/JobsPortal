package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Filters;
import com.tuespotsolutions.entity.JobFilter;
import com.tuespotsolutions.entity.JobFilterValues;

@Repository
public interface JobFilterValuesRepository extends JpaRepository<JobFilterValues, Long>{

	List<JobFilterValues> findByFilterValueId(Long id);

	List<JobFilterValues> findByJobFilter(JobFilter item);
	
	
	@Query(value = "SELECT count(jv.filter_value_id)as jobCount, jv.filter_value_id as filterValueId ,f.filter_value as filterValue, f.filter_id as filterId "
			+ "FROM jobfiltervalue as jv \r\n"
			+ "inner join filtervalue as f on jv.filter_value_id = f.id group by jv.filter_value_id having f.filter_id in (select jbf.filter_id from jobfilter as jbf where jbf.job_id =:jobId)"
			, nativeQuery = true)
	public List<getFilterCountCityWise> getFilterJobCountCityWise(@Param("jobId") Long jobId);

	
	public interface getFilterCountCityWise{
		public Long getJobCount();
		public Long getFilterValueId();
		public String getFilterValue();
		public Long getFilterId();
	}
}
