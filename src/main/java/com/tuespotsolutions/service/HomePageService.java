package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.CompanyLogo;
import com.tuespotsolutions.models.HomePagePills;
import com.tuespotsolutions.models.HomePageRandomJobs;


public interface HomePageService {

	public List<HomePageRandomJobs> getRendomJobForHomePage();
	
	public List<HomePagePills> getFilterList();
	
	public List<CompanyLogo> companyLogo();
	
}
