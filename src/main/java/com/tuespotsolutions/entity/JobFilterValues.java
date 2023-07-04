package com.tuespotsolutions.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "jobfiltervalue")
public class JobFilterValues {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long filterValueId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private JobFilter jobFilter;
	
}
