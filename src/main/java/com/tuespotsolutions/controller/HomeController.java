package com.tuespotsolutions.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.CompanyLogo;
import com.tuespotsolutions.models.HomePagePills;
import com.tuespotsolutions.models.HomePageRandomJobs;
import com.tuespotsolutions.models.JobResponse;
import com.tuespotsolutions.service.HomePageService;

@RestController
@CrossOrigin("*")
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
	private HomePageService homePageService;
	
	@GetMapping("/page/job")
	public ResponseEntity<?> getRandomJobsForHomePage(){
		 List<HomePageRandomJobs> rendomJobForHomePage = this.homePageService.getRendomJobForHomePage();
		return new ResponseEntity<List<HomePageRandomJobs>>(rendomJobForHomePage, HttpStatus.OK);
	}
	
	@GetMapping("/pills")
	public ResponseEntity<?> getHomePageTopPills(){
		List<HomePagePills> filterList = this.homePageService.getFilterList();
		return new ResponseEntity<List<HomePagePills>>(filterList, HttpStatus.OK);
	}
	
	@GetMapping("company/logo")
	public ResponseEntity<?> getRandomCompanyLogoList(){
		List<CompanyLogo> companyLogo = this.homePageService.companyLogo();
		return new ResponseEntity<List<CompanyLogo>>(companyLogo, HttpStatus.OK);
	}

}
