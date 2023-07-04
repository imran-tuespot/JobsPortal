package com.tuespotsolutions.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.PackagesResponse;
import com.tuespotsolutions.service.WebsitePackagesService;

@RestController
@CrossOrigin("*")
@RequestMapping("/websitepackages")
public class WebsitePackagesController {

	
	@Autowired
	private WebsitePackagesService packagesService;
	
	@GetMapping("for/company")
	public ResponseEntity<?> getCompanyPackagesList(){
		List<PackagesResponse> findPackagesByCompany = this.packagesService.findPackagesByCompany();
		return new ResponseEntity<List<PackagesResponse>>(findPackagesByCompany, HttpStatus.OK);
	}
	
	
	@GetMapping("for/candidate")
	public ResponseEntity<?> getCandidatePackagesList(){
		List<PackagesResponse> findPackagesByCompany = this.packagesService.findPackagesByCompany();
		return new ResponseEntity<List<PackagesResponse>>(findPackagesByCompany, HttpStatus.OK);
	}
	
}
