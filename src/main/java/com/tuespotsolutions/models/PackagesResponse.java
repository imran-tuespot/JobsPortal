package com.tuespotsolutions.models;


import java.util.List;

import lombok.Data;

@Data
public class PackagesResponse {
	
	private Long id;
	private String name;
	private Integer days;
	private double price;
	//free/paid
	private String type;
	private double discount;
	private boolean company;
	private boolean candidate;
	private boolean status;
	private boolean defaultPackage;
	private List<PackageDiscriptionResponse> discription;

}
