package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.CandidateProjects;

@Repository
public interface CandidateProjectsRepository extends JpaRepository<CandidateProjects, Long> {

	public List<CandidateProjects> findByCandidate(Candidate candidate);
	
}
