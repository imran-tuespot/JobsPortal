package com.tuespotsolutions.repository;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.CompanyInboxNotification;

@Repository
public interface CompanyInboxNotificationRepository extends JpaRepository<CompanyInboxNotification, Long>{

    public	List<CompanyInboxNotification> findByCompanyId(Long companyId);
	
//    @Query(value = "select * from companyinboxnotification  where candidate_id= :companyId order by id DESC LIMIT 10" , nativeQuery = true)
//    public List<CompanyInboxNotification> getLatestNotification(@PathParam("companyId") long companyId);
    
    @Query(value = "select * from CompanyInboxNotification   where company_id= :companyId and status='NOTIFICATION_RECIVED' order by id DESC limit 10", nativeQuery = true)
    public List<CompanyInboxNotification> getLatestNotification(@PathParam("companyId") long companyId);
    
    public CompanyInboxNotification findByStatus(String status);

	public Page<CompanyInboxNotification> findByCompanyId(Pageable pageable, long companyId);
}


