package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.Job;
import com.tuespotsolutions.entity.JobApplied;

@Repository
public interface JobAppliedRepository extends JpaRepository<JobApplied, Long> {
	
    List<JobApplied> findByCandidates(Candidate candidate);
    List<JobApplied> findByJob(Job job);
    
    long countByCompanyId(long companyId);
    
}
