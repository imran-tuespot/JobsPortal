package com.tuespotsolutions.entity;

import java.util.Date;

import javax.persistence.CascadeType;
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


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jobapplied")
public class JobApplied {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date createdOn;
	private Date modifiedOn;
	private boolean status;
	private Long companyId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Job job;
	
	@ManyToOne
	private Candidate candidates;
	

}
