package com.tuespotsolutions.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "transcation")
public class Transcation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long planeId;
	private String planeAmount;
	private Long userId;
	private String type;
	private String transactionId;
	private String tokenTranscationId;
	@Column(name = "transaction_response", columnDefinition = "TEXT")
	private String transactionResponse;
	private String customerEmail;
	private String customerMobileNumber;
	private Date createdOn;
	private Date modifiedOn;
	private String status;
	
}
