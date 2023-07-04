package com.tuespotsolutions.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDiscriptionResponse {

	private long id;
	private String description;
	private long packageId;
}
