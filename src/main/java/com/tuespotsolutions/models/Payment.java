package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class Payment {
	
	private String name;
	private String email;
	private String mobileNumber;
	private double amount;
	private Long userId;
	private Long planeId;
	
}
