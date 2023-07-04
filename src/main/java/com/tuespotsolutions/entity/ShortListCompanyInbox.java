package com.tuespotsolutions.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "shortlistcompanyinbox")
public class ShortListCompanyInbox {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long candidateId;
	private Long jobId;
	private Long companyId;
	private Date createdOn;
	private Date modifiedOn;
	private String status;
	private Long notificationId;
	
}
