package com.tuespotsolutions.models;


import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class PackagesRequest {

	private Long id;
	@NotEmpty(message = "Package name field must be required")
	private String name;
	private String discription;
	private Integer days;
	private double price;
	//free/paid
	private String type;
	private double discount;
	private boolean company;
	private boolean candidate;
	private boolean status;
	private boolean defaultPackage;
	private Long userId;
	
	
}
