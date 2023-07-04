package com.tuespotsolutions.models;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDiscriptionModel {
	
	private long id;
	@NotEmpty(message = "Description Field is Required")
	private String description;
	private long packageId;

}
