package com.tuespotsolutions.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfileResponse {
	
	private Long id;
	private String name;
	private String email;
	private String mobileNumber;
	private String address;
	private Integer city;
	private Integer district;
	private Integer state;
	private Long pinCode;
	private String panCard;
	private String bussinessLocation;
	private String gstNo;
	private String logo;
	
}
