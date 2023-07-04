package com.tuespotsolutions.models;

import java.util.List;

import lombok.Data;

@Data
public class HomePagePills {
	private Long id;
	private String pillsTitle;
	List<HomePagePillsValue> pillsValue;
}
