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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
 *   Entity    : Company 
 *   CratedBy  : Baljinder Singh
 *   CreatedOn : 28/Feb/2023
 *   
 * */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class Company {

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
	private String bussinessLocation;
	private String gstNo;
	private String logo;
	private String interviewLink;
	private Date createdOn;
	private Date modifiedOn;
	private boolean status;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "company")
	private List<Job> job;
	
}
