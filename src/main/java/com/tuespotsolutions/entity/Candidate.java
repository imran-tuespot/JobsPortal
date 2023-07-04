package com.tuespotsolutions.entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/*
 *   Entity    : Candidate 
 *   CratedBy  : Baljinder Singh
 *   CreatedOn : 28/Feb/2023
 *   
 * */


@Data
@Entity
@Table(name = "candidate")
public class Candidate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String username;
	private String email;
	private String mobileNumber;
	private String address;
	private Integer city;
	private Integer district;
	private Integer state;
	private Long pinCode;
	private String panCard;
	private String resume;
	private Date dateOfBirth;
	private boolean status;
	private Date createdOn;
	private Date modifiedOn;
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	private String image;
	private String profileHeadline;
	
	@OneToMany(cascade = CascadeType.ALL,  mappedBy = "candidate")
	private List<CandidateEducation> candidateEducation;
	
	@OneToMany(cascade = CascadeType.ALL,  mappedBy = "candidate")
	private List<CandidateExperience> candidateExperiences;
	
	@OneToMany(cascade = CascadeType.ALL,  mappedBy = "candidate")
	private List<CandidateLanguages> candidateLanguages;
	
	@OneToMany(cascade = CascadeType.ALL,  mappedBy = "candidate")
	private List<CandidateProjects> candidateProjects;
	
	@OneToMany(cascade = CascadeType.ALL,  mappedBy = "candidate")
	private List<CandidateSalary> candidateSalaries;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "candidates")
	private List<JobApplied> jobApplieds;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "candidate")
	private List<CandidateSkills> candidateSkills;

}
