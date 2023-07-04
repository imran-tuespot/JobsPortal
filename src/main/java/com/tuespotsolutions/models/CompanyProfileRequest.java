package com.tuespotsolutions.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CompanyProfileRequest {
	
	private Long id;
	@NotEmpty(message = "Company name field is required")
	private String name;
	
	@Email(message = "Email address is not valid")
	@NotEmpty(message = "Email field is required")
	private String email;
	
	@NotEmpty(message = "Mobile Number field is required")
	@Size(min = 10, max = 10 , message = "Mobile number must contain 10 digits")
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
