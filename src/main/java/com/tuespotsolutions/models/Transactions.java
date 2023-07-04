package com.tuespotsolutions.models;

import java.util.Date;

import lombok.Data;

@Data
public class Transactions {
	
	private Long id;
	private String transactionId;
	private String tokenTranscationId;
	private String customerEmail;
	private String customerMobileNumber;
	private Date createdOn;
	private Date modifiedOn;
	private String status;
}
