package com.tuespotsolutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	boolean existsByUserName(String username);
}
