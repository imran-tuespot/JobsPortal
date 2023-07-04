package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.FilterValues;
import com.tuespotsolutions.entity.Filters;

@Repository
public interface FilterValueRepository extends JpaRepository<FilterValues, Long>{

	List<FilterValues> findByFilter(Filters filters);
	
}
