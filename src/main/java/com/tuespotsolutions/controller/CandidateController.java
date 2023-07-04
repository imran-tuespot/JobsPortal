package com.tuespotsolutions.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.tuespotsolutions.models.CandiateListWithPagination;
import com.tuespotsolutions.models.CandidateRegistrationResponse;
import com.tuespotsolutions.models.CandidateRequest;
import com.tuespotsolutions.models.OtpConfirmedRequest;
import com.tuespotsolutions.models.SearchPeopleDetail;
import com.tuespotsolutions.models.SearchedPeopleResponse;
import com.tuespotsolutions.service.CandidateRegistrationService;
import com.tuespotsolutions.service.OtpConfirmationService;

@RestController
@CrossOrigin("*")
@RequestMapping("/candidate")
public class CandidateController {

	Logger logger = LoggerFactory.getLogger(CandidateController.class);

	@Autowired
	private CandidateRegistrationService candidateRegistrationService;
	

	@Autowired
	private OtpConfirmationService otpConfirmationService;

	@PostMapping("/registeration")
	public ResponseEntity<?> registerCandidate(@Valid @RequestBody CandidateRequest candidateRequest) {
		logger.info("line no : 40 registerCandidate() method");
		Map<String, String> registerCandidate = this.candidateRegistrationService.registerCandidate(candidateRequest);
		return new ResponseEntity<Map<String, String>>(registerCandidate, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateCandidate(@Valid @RequestBody CandidateRegistrationResponse candidateRequest) {
		logger.info("line no : 50 registerCandidate() method");
		Map<String, String> registerCandidate = this.candidateRegistrationService.updateCandidate(candidateRequest);
		return new ResponseEntity<Map<String, String>>(registerCandidate, HttpStatus.OK);
	}

	@PostMapping("/enterotp")
	public ResponseEntity<?> enterOtp(@RequestBody OtpConfirmedRequest confirmedRequest) {
		logger.info("line no : 47 enterOtp() method");
		CandidateRegistrationResponse otpConfirmationForCandidate = this.otpConfirmationService
				.otpConfirmationForCandidate(confirmedRequest);
		return new ResponseEntity<CandidateRegistrationResponse>(otpConfirmationForCandidate, HttpStatus.ACCEPTED);
	}

	@GetMapping("/by")
	public ResponseEntity<?> getCandidateById(@RequestParam("candidateId") long candidateId) {
		CandidateRegistrationResponse candidateRegistrationResponse = this.candidateRegistrationService
				.getCandidate(candidateId);
		return new ResponseEntity<CandidateRegistrationResponse>(candidateRegistrationResponse, HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<?> getCandidteList(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		CandiateListWithPagination candidateList = this.candidateRegistrationService.getCandidateList(pageNumber,
				pageSize);
		return new ResponseEntity<CandiateListWithPagination>(candidateList, HttpStatus.OK);
	}

	@PostMapping("/searchpeople")
	public ResponseEntity<?> seachPeopleWithProfileHandle(@RequestBody String profileHandle) {
		List<SearchedPeopleResponse> searchedPeople = this.candidateRegistrationService.searchedPeople(profileHandle);
		return new ResponseEntity<List<SearchedPeopleResponse>>(searchedPeople, HttpStatus.OK);
	}

	@GetMapping("/searchpeople/get/detail/by")
	public ResponseEntity<?> searchedpeopleDetailById(@RequestParam("candidateId") long candidateId) {
		SearchPeopleDetail searchedpeopleDetailById = this.candidateRegistrationService
				.searchedpeopleDetailById(candidateId);
		return new ResponseEntity<SearchPeopleDetail>(searchedpeopleDetailById, HttpStatus.OK);
	}

	
}
