package com.tuespotsolutions.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String applicantName;
	private String username;
	private String email;
	private String mobileNumber;
	private String password;
	private Date createdOn;
	private Date modifiedOn;
	private String userType;
	private Long typeId;
	private boolean status;
	 @ManyToMany(fetch = FetchType.EAGER)
	  @JoinTable(  name = "user_roles", 
	        joinColumns = @JoinColumn(name = "user_id"), 
	        inverseJoinColumns = @JoinColumn(name = "role_id"))
	  private Set<Roles> roles = new HashSet<>();

}
