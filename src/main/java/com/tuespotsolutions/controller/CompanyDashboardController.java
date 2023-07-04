package com.tuespotsolutions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.CompanyDashboard;
import com.tuespotsolutions.service.CompanyDashboardService;

@RestController
@CrossOrigin("*")
@RequestMapping("/company/dashboard")
public class CompanyDashboardController {

	
	@Autowired
	private CompanyDashboardService companyDashboardService;
	
	
	@GetMapping("/cout/by")
	public ResponseEntity<?> getDashboardCounts(@RequestParam("companyId") Long companyId){
		CompanyDashboard dashboardDataOfCompany = this.companyDashboardService.getDashboardDataOfCompany(companyId);
		return new ResponseEntity<CompanyDashboard>(dashboardDataOfCompany, HttpStatus.OK);
	}
	
}
