package com.tuespotsolutions.models;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UpdateCompanyInterViewLink {
	
	private long companyId;
	@NotEmpty(message = "Interview Link Field is required field")
	private String interViewLink;

}
