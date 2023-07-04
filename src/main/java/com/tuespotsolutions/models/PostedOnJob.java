package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class PostedOnJob {
	private String postedOn;
	private String jobCount;
	private String jobTitle;
	private Integer number;
	private boolean status = false;
}
