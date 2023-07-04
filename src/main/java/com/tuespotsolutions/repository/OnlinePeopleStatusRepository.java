package com.tuespotsolutions.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.OnlinePeopleStatus;

@Repository
public interface OnlinePeopleStatusRepository extends JpaRepository<OnlinePeopleStatus, Long> {
	
	public Optional<OnlinePeopleStatus> findByUserId(long userId);

}
