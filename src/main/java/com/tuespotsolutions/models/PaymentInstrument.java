package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class PaymentInstrument {

	private String entity;
	private String id;
	private String name;
	private String type_id;
	private String type_name;
	
}
