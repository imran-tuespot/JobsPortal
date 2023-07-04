package com.tuespotsolutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.CandidateLanguages;

@Repository
public interface CandidateLanguagesRepository extends JpaRepository<CandidateLanguages, Long>{

}
