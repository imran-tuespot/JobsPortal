package com.tuespotsolutions.repository;

import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.CandidateInboxNotification;
import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.Job;

@Repository
public interface CandidateInboxNotificationRepository extends JpaRepository<CandidateInboxNotification, Long>{
	
	public List<CandidateInboxNotification> findByCandidateId(Long candidateId);
	public Optional<CandidateInboxNotification> findByCandidateIdAndCompanyIdAndJobId(Long candidateId, Long companyId, Long jobId);
	
	public Optional<CandidateInboxNotification> findByCandidateIdAndCompanyId(Long candidateId, Long companyId);
	
	 @Query(value = "select * from CandidateInboxNotification   where candidate_id=:candidateId and status='NOTIFICATION_RECIVED' order by id DESC limit 10", nativeQuery = true)
	    public List<CandidateInboxNotification> getLatestInboxNotification(@PathParam("candidateId") Long candidateId);
	 
	 public CandidateInboxNotification findByStatus(String status);
	 
	 long countByCompanyId(Long companyId);
	public Page<CandidateInboxNotification> findByCandidateId(Pageable pageable, Long candidateId);
	
}
