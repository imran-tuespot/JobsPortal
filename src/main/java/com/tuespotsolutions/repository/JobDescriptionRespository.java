package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Job;
import com.tuespotsolutions.entity.JobDescription;

@Repository
public interface JobDescriptionRespository extends JpaRepository<JobDescription, Long>{

	List<JobDescription> findByJob(Job job);
	
}
