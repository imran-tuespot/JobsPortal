package com.tuespotsolutions.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {

	@NotEmpty(message = "Company name field is required")
	private String name;
	
	@NotEmpty(message = "Username field is required")
	private String userName;

	@Email(message = "Email address is not valid")
	@NotEmpty(message = "Email field is required")
	private String email;
	
	@NotEmpty(message = "Mobile Number field is required")
	@Size(min = 10, max = 10 , message = "Mobile number must contain 10 digits")
	private String mobileNumber;
	private String address;
	private Integer stateId;
	private Integer districtId;
	private Integer cityId;
	private Long pinCode;
	private String panCardNumber;
	private String businessLocation;
	private String gstNumber;
	private String logo;

	@NotEmpty
	@Size(min = 3, message = "Password must be atleast minimum of 3 characters")
	private String password;

}
