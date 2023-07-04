package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class SearchedPeopleResponse {
	
	private Long id;
	private String name;
	private String location;
	private String profileImage;
	private String profileHeadline;
	private String lastSeen;

}
