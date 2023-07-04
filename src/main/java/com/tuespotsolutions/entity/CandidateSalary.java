package com.tuespotsolutions.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 *   Entity    : CandidateSalary 
 *   CratedBy  : Baljinder Singh
 *   CreatedOn : 28/Feb/2023
 *   
 * */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidate_salary")
public class CandidateSalary {
	
		@Id
		@GeneratedValue
		private Long id;
		private String currentCtc;
		private String expectedCtc;
		private String noticePeriod;
		
		@ManyToOne
		private Candidate candidate;
}
