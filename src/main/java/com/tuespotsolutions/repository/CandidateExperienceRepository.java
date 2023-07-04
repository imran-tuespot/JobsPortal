package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.CandidateExperience;

@Repository
public interface CandidateExperienceRepository extends JpaRepository<CandidateExperience, Long> {

	public List<CandidateExperience> findByCandidate(Candidate candidate);
	
}
