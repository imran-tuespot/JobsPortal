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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "job")
public class Job {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	// Remote / office
	private String location;
	// partime / fulltime
	private String jobType;
	private String experience;
	private String skills;
	private String createOn;
	private String modifiedOn;
	
	//active/ inactive
	private boolean status;
	
	private String department;
	
	@ManyToOne
	private Company company;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "job")
	private List<JobDescription> descriptionInPoints;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "job")
	private List<JobApplied> applied;

}
