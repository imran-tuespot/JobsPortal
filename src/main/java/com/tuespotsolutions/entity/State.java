package com.tuespotsolutions.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/*
 *   Entity    : State 
 *   CratedBy  : Baljinder Singh
 *   CreatedOn : 28/Feb/2023
 *   
 *   
 *   Description : Need sql file to upload the state data. 
 *                 sql file location src/main/resources folder indiaDb fileName : "indian_state_district_city.sql"
 *                 
 *                 command to upload sql file as below
 *                 
 *                 file Location take from development system
 *                 
 *                 mysql>source H:\JobPortalBackendApplication\IndiaDB\indian_state_district_city.sql
 *   
 * */

@Data
@Entity
@Table(name = "state")
public class State {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "state_id")
	private Integer stateId;
	
	@Column(name = "state_title", length = 100)
	private String stateTitle;
	
	@Column(name = "state_description", columnDefinition = "TEXT")
	private String stateDescription;
	
	@Column(name = "status", length = 10)
	private String status;
	

}
