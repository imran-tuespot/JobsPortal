package com.tuespotsolutions.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.RegistrationOtp;

@Repository
public interface RegistrationOtpRepository extends JpaRepository<RegistrationOtp, Long> {
	
	Optional<RegistrationOtp> findByUserIdAndUserType(Long userId, String userType);
	
}
