package com.tuespotsolutions.entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

@Data
@Entity
@Table(name = "packages")
public class Packages {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Integer days;
	private double price;
	//free/paid
	private String type;
	private double discount;
	private boolean company;
	private boolean candidate;
	private Date createdOn;
	private Date modifiedOn;
	private Long createdBy;
	private Long modifiedBy;
	private boolean status;
	private boolean defaultPackage;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "packages")
	private List<PackageDiscription> packageDiscriptions;
    
}
