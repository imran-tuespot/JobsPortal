package com.tuespotsolutions.models;

import java.util.List;

import lombok.Data;

@Data
public class PackageWithActiveStatusListForCompany {
	
	private Long id;
	private String name;
	private Integer days;
	private double price;
	private String type;
	private double discount;
	private Integer  pendingDays;
	private boolean status;
	private String startDate;
	private String endDate;
	private Integer pendingDaysPercentage;
	private Long packageId;
	private List<PackageDiscriptionResponse> discription;
	
}
