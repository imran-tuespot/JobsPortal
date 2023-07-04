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
@Table(name = "paymentgateways")
public class PaymentGateways {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String paymentGatwayName;
	private String paymentGatwayLogo;
	private Boolean status;
	private Date createdOn;
	private Date modifiedOn;
	private String paymentGatewayUrl;
	
}
