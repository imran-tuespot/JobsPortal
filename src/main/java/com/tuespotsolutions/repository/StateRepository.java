package com.tuespotsolutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Integer>{

}
