package com.tuespotsolutions.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {
	
	private Long id;
	private String name;
	private String email;
	private String mobileNumber;
	private String logo;
}
