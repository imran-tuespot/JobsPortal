package com.tuespotsolutions.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/*
 *   Entity    : District 
 *   CratedBy  : Baljinder Singh
 *   CreatedOn : 28/Feb/2023
 *   
 *    Description : Need sql file to upload the district data. 
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
@Table(name = "district")
public class District {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "districtid")
	private Integer districtId;
	
	@Column(name = "district_title", length = 100)
	private String districtTitle;
	
	@Column(name = "state_id")
	private Integer stateId;
	
	@Column(name = "district_description", columnDefinition = "TEXT")
	private String districtDescription;
	
	@Column(name = "district_status", length = 10)
	private String districtStatus;
}
