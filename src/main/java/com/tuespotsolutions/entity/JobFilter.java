package com.tuespotsolutions.entity;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "jobfilter")
public class JobFilter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Job job;
	
	@ManyToOne
	@JoinColumn(name = "filter")
	private Filters filter;
	
	//private Long filterId;
	
	@ManyToOne
	private FilterValues filterValue;
}
