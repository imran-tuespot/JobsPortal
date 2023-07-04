package com.tuespotsolutions.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
 *   Entity    : CandidateProjects 
 *   CratedBy  : Baljinder Singh
 *   CreatedOn : 28/Feb/2023
 *   
 * */


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidate_projects")
public class CandidateProjects {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String client;
	private boolean projectStatus;
	private Date startDate;
	private Date endDate;
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	private String location;
	private String natureOfEmployement;
	private Integer teamSize;
	private String role;
	@Column(name = "role_description", columnDefinition = "TEXT")
	private String roleDescription;
	private String skillSet;

	@ManyToOne
	private Candidate candidate;

}
