package com.tuespotsolutions.entity;

import java.sql.Date;

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
 *   Entity    : CandidateExperience 
 *   CratedBy  : Baljinder Singh
 *   CreatedOn : 28/Feb/2023
 *   
 * */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidate_experience")
public class CandidateExperience {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private boolean current;
	private String employmentType;
	private String companyName;
	private String designation;
	private Date joingDate;
	private Date endDate;
	private String ctc;
	private String jobProfile;
	
	@ManyToOne
	private Candidate candidate;
}
