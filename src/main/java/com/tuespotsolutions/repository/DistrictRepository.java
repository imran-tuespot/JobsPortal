package com.tuespotsolutions.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
	
	List<District> findByStateId(Integer stateId);
	
}
