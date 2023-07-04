package com.tuespotsolutions.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Packages;

@Repository
public interface PackagesRepository extends JpaRepository<Packages, Long>{
		
	public	Page<Packages> findByCompany(Pageable pageable, boolean status);
	public	Page<Packages> findByCandidate(Pageable pageable, boolean status);
	
	public	List<Packages> findByCandidate(boolean status);
	public	List<Packages> findByCompany(boolean status);
	
	public Optional<Packages> findByCandidateAndDefaultPackage(boolean candidateStatus, boolean defaultPackageStatus);
	
	public Optional<Packages> findByCompanyAndDefaultPackage(boolean candidateStatus, boolean defaultPackageStatus);
	
	public boolean existsByName(String packageName);
		
}
