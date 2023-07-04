package com.tuespotsolutions.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.CompanyDetailResponse;
import com.tuespotsolutions.models.CompanyListWithPagination;
import com.tuespotsolutions.models.CompanyProfileRequest;
import com.tuespotsolutions.models.CompanyProfileResponse;
import com.tuespotsolutions.models.CompanyRequest;
import com.tuespotsolutions.models.CompanyResponse;
import com.tuespotsolutions.models.OtpConfirmedRequest;
import com.tuespotsolutions.models.UpdateCompanyInterViewLink;
import com.tuespotsolutions.service.CompanyRegistrationService;
import com.tuespotsolutions.service.OtpConfirmationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin("*")
@RequestMapping("/company")

public class CompanyController {

	Logger logger = LoggerFactory.getLogger(CompanyController.class);

	@GetMapping("/test")
	public String hello() {
		logger.trace("A TRACE Message");
		logger.debug("A DEBUG Message");
		logger.info("An INFO Message");
		logger.warn("A WARN Message");
		logger.error("An ERROR Message");
		return "Hello Scaler!";
	}

	@Autowired
	private CompanyRegistrationService companyRegistrationService;

	@Autowired
	private OtpConfirmationService otpConfirmationService;

	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> registerCompany(@Valid @RequestBody CompanyRequest companyRequest) {
		logger.info("line no : 58 registerCompany() method");
		Map<String, String> registerCompany = this.companyRegistrationService.registerCompany(companyRequest);
		return new ResponseEntity<Map<String, String>>(registerCompany, HttpStatus.OK);
	}

	@PostMapping("/enterotp")
	public ResponseEntity<?> enterOtp(@RequestBody OtpConfirmedRequest confirmedRequest) {
		logger.info("line no : 66 enterOtp() method");
		CompanyResponse otpConfirmation = this.otpConfirmationService.otpConfirmation(confirmedRequest);
		return new ResponseEntity<CompanyResponse>(otpConfirmation, HttpStatus.ACCEPTED);
	}

	@GetMapping("/profile")
	public ResponseEntity<?> getCompanyProfile(@RequestParam("companyId") long companyId) {
		CompanyProfileResponse companyProfileDetails = this.companyRegistrationService
				.getCompanyProfileDetails(companyId);
		return new ResponseEntity<CompanyProfileResponse>(companyProfileDetails, HttpStatus.OK);
	}

	@PutMapping("/update/profile")
	public ResponseEntity<?> updateComanyProfile(@Valid @RequestBody CompanyProfileRequest companyProfileResponse) {
		Map<String, String> updateCompanyProfile = this.companyRegistrationService
				.updateCompanyProfile(companyProfileResponse);
		return new ResponseEntity<Map<String, String>>(updateCompanyProfile, HttpStatus.OK);
	}
	
	@GetMapping("/list")
	public ResponseEntity<?> companyList(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
			){
		CompanyListWithPagination companyList = this.companyRegistrationService.getCompanyList(pageNumber, pageSize);
		return new ResponseEntity<CompanyListWithPagination>(companyList, HttpStatus.OK);
	}
	
	@GetMapping("/deatil")
	public ResponseEntity<?> getCompanyDetail(@RequestParam("companyId") long companyId) {
		 CompanyDetailResponse response = this.companyRegistrationService
				.getCompanyDetailById(companyId);
		return new ResponseEntity<CompanyDetailResponse>(response, HttpStatus.OK);
	}
	
	@PutMapping("/update/interviewlink")
	public ResponseEntity<?> updateInterViewLink(@Valid @RequestBody UpdateCompanyInterViewLink companyInterViewLink){
		Map<String, String> updateInterViewLink = this.companyRegistrationService.updateInterViewLink(companyInterViewLink);
		return new ResponseEntity<Map<String, String>>(updateInterViewLink, HttpStatus.OK);
	}
}
