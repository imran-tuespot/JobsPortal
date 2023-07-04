package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.CandidateSkills;

public interface CandidateSkillsRepository extends JpaRepository<CandidateSkills, Long> {

	public List<CandidateSkills> findByCandidate(Candidate candidate);
	
}
