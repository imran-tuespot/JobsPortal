package com.tuespotsolutions.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.AssignedPackages;

@Repository
public interface AssignedPackagesRepository extends JpaRepository<AssignedPackages, Long> {
	
		public AssignedPackages findByUserId(Long userId);
		
		public List<AssignedPackages> findByUserIdAndUserType(Long userId, String userType);
		
		public List<AssignedPackages> findByUserType(String userType);
		
}
