package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Filters;

@Repository
public interface FilterRepository extends JpaRepository<Filters, Long> {

	public List<Filters> findByStatus(Boolean status);

	@Query(value = "select count(jobs.title) as jobCount ,city.name, city.id  from\r\n" + "      city as city\r\n"
			+ "	  right join job  as jobs\r\n"
			+ "       on city.id = jobs.city_id  where jobs.title like %:title% and jobs.status = true group by jobs.city_id", nativeQuery = true)
	public List<JobCountByCity> findJobCountByCity(@Param("title") String title);

	public interface JobCountByCity {
		public String getJobCount();

		public String getName();

		public String getId();

	}

	@Query(value = "select count(*) as jobCount, jobworkmode.title, jobworkmode.id from JobWorkMode jobworkmode\r\n"
			+ "right join Job as jobs on  jobworkmode.id = jobs.work_mode_id where jobs.title like %:title% and jobs.status = true group by jobs.work_mode_id", nativeQuery = true)
	public List<JobCountByWorkMode> findJobCountByWorkMode(@Param("title") String title);

	public interface JobCountByWorkMode {

		public String getJobCount();

		public String getTitle();

		public Long getId();

	}
}
