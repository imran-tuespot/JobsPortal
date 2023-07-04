package com.tuespotsolutions.models;

import java.util.Date;

import lombok.Data;

@Data
public class PaymentGatewaysRequest {
	
	private Long id;
	private String paymentGatwayName;
	private String paymentGatwayLogo;
	private Boolean status;
	private Date createdOn;
	private Date modifiedOn;
	private String paymentGatewayUrl;

}
	