package com.tuespotsolutions.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {

      Boolean existsByName(String candidateName);
      Boolean existsByUsername(String username);
	  Boolean existsByEmail(String email);
	  Boolean existsByMobileNumber(String mobileNumber);
	  Optional<Candidate> findByEmail(String email);
	  
	  Optional<Candidate> findByMobileNumber(String mobileNumber);
	  Optional<Candidate> findByUsername(String username);
	  Optional<Candidate> findByName(String username);
	
}
