package com.tuespotsolutions.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "assignedpackages")
public class AssignedPackages {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long packageId;
	private String assignDate;
	private String endDate;
	private Integer  assignedDays;
	private Integer pendingDays;
	private Long userId;
	private String userType;
	private boolean status;

}
