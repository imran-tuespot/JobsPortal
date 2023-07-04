package com.tuespotsolutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.JobWorkMode;

@Repository
public interface JobWorkModeRepository extends JpaRepository<JobWorkMode, Long> {

}
