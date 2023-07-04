package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.CandidateSalary;

@Repository
public interface CandidateSalaryRepository extends JpaRepository<CandidateSalary, Long> {

	public List<CandidateSalary> findByCandidate(Candidate candidate);
}
