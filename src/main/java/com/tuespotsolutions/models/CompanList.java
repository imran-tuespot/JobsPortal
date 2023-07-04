package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class CompanList {

	private Long id;
	private String name;
	private String email;
	private String mobileNumber;
	private String address;
	private String city;
	private String district;
	private String state;
	private Long pinCode;
	private String panCard;
	private String bussinessLocation;
	private String gstNo;
	private String logo;
}
