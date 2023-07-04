package com.tuespotsolutions.entity;

import java.util.Date;

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
 *   Entity    : CandidateEducation 
 *   CratedBy  : Baljinder Singh
 *   CreatedOn : 28/Feb/2023
 *   
 * */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidate_education")
public class CandidateEducation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String university;
	private String course;
	private String specialization;
	private String courseType;
	private String courseDuration;
	private String gradingSystem;
	private Double marks;
	
	@ManyToOne
	private Candidate candidate;

}
