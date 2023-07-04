package com.tuespotsolutions.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "registration_otp")
public class RegistrationOtp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String otpRefrenceId;
	private String otp;
	private Long userId;
	private String userType;
	private Date createdOn;
	private Date modifiedOn;
	
}
