package com.tuespotsolutions.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Transcation;

@Repository
public interface TranscationRepository extends JpaRepository<Transcation, Long>{

	public Optional<Transcation> findByTransactionId(String transactionId);
	
	public Page<Transcation> findByPlaneIdAndType(Long planId,String type,Pageable pageable);
	
}
