package com.tuespotsolutions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.PackageDiscription;
import com.tuespotsolutions.entity.Packages;

@Repository
public interface PackageDiscriptionRepository extends JpaRepository<PackageDiscription, Long> {

	public Page<PackageDiscription> findByPackages(Pageable pageable, Packages packages);
	
}
