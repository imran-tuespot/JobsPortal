package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

	public Page<Job> findByCompany(Pageable pageable, Company company);

	@Query("Select j.location from Job j")
	public List<String> findLocation();
	
	@Query(value = "SELECT * FROM job where status=true  order by rand() limit 6", nativeQuery = true)
	public List<Job> getRandomJobs();
	

//	@Query("Select j.jobType from Job j")
//	public List<String> findWorkMode();

	@Query("Select j.department from Job j")
	public List<String> findDepartment();

//	@Query("Select j.type from Job j")
//	public List<String> findType();
	
	public List<Job> findByLocation(String location);

	public List<Job> findByJobType(String jobType);

	public List<Job> findByDepartment(String department);

	//public List<Job> findByType(String type);

	long countByCompanyAndStatus(Company company, Boolean status);

	public List<Job> findByCompany(Company company);

	
	public long countByIdAndTitleContaining(Long cityId, String title);
	

//	@Query(value = "Select j from Job j where j.cityId=:cityId and j.title LIKE  '%'||:title||'%' and j.status=true")
//	public List<Job> getJobByCityAndTitle(@Param("cityId") Integer cityId, @Param("title") String title);

//	public Long countByCityId(Long cityId);

	@Query(value = "select count(*) from job  where create_on between date_sub(now(),INTERVAL 1 DAY) and now() and title LIKE  %:title% and status=true", nativeQuery = true)
	public Integer getLastDayJobCount(@Param("title") String title);

	@Query(value = "select count(*) from job  where create_on between date_sub(now(),INTERVAL 1 WEEK) and now() and title LIKE  %:title% and status=true", nativeQuery = true)
	public Integer getLastWeekJobCount(@Param("title") String title);

	@Query(value = "select count(*) from job  where create_on between date_sub(now(),INTERVAL 1 MONTH) and now() and title LIKE  %:title% and status=true", nativeQuery = true)
	public Integer getLastMonthJobCount(@Param("title") String title);

	@Query(value = "select count(*) from job  where create_on between date_sub(now(),INTERVAL 1 YEAR) and now() and title LIKE  %:title% and status=true", nativeQuery = true)
	public Integer getLastYearJobCount(@Param("title") String title);
	
	@Query(value = "select * from job  where create_on between date_sub(now(),INTERVAL 1 DAY) and now() and title LIKE  %:title% and status=true", nativeQuery = true)
	public List<Job> findJobByPostedOnLastDay(@Param("title") String title);
	
	@Query(value = "select * from job  where create_on between date_sub(now(),INTERVAL 1 WEEK) and now() and title LIKE  %:title% and status=true", nativeQuery = true)
	public List<Job> findJobByPostedOnLastWeek(@Param("title") String title);
	
	@Query(value = "select * from job  where create_on between date_sub(now(),INTERVAL 1 MONTH) and now() and title LIKE  %:title% and status=true", nativeQuery = true)
	public List<Job> findJobByPostedOnLastMonth(@Param("title") String title);
	
	@Query(value = "select * from job  where create_on between date_sub(now(),INTERVAL 1 YEAR) and now() and title LIKE  %:title% and status=true", nativeQuery = true)
	public List<Job> findJobByPostedOnLastYear(@Param("title") String title);

	@Query(value="select count(*) from jobfiltervalue where filter_value_id=:filterID", nativeQuery = true)
	public Long findByJobsfilterID(@Param("filterID") Long filterID );
	
	@Query(value = "select * from job jb left join jobfilter jbf on jb.id = jbf.job_id where title like %:jobTitle% and jbf.filter =:filter_id and jbf.filter_value_id =:filter_value_id", nativeQuery = true)
    public List<Job> getJobIdByFilterIdAndFilterValueId(@Param("jobTitle") String jobTitle, @Param("filter_id") long filter_id, @Param("filter_value_id") long filter_value_id);
    
//    public interface getJobIdWithFilterIdAndFilterValueId{
//    	public Long getJobId();
//    }
}
