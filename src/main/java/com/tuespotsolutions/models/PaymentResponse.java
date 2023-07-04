package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class PaymentResponse {
	
	private String amount;
	private String currency;
	private String payment_error_code;
	private String payment_error_description;
	private String vpa;
	private String sub_accounts_id;
	private String id;
	private String entity;
	private String status;
	
	private PaymentInstrument payment_instrument;
	private PaymentToken payment_token;
	private CustomerDetail customer;
	
	
}
