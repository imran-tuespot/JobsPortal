package com.tuespotsolutions.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	boolean existsByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);
	Optional<User> findByUserTypeAndTypeId(String userType, Long typeId);
	
}
