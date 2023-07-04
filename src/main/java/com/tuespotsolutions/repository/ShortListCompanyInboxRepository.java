package com.tuespotsolutions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.ShortListCompanyInbox;

@Repository
public interface ShortListCompanyInboxRepository extends JpaRepository<ShortListCompanyInbox, Long> {

	List<ShortListCompanyInbox> findByCompanyId(Long companyId);
	
}
