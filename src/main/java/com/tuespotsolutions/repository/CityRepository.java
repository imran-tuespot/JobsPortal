package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer>{
	
		List<City> findByDistrictId(Integer districtId);
}
