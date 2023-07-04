package com.tuespotsolutions.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.CandidateEducation;
import com.tuespotsolutions.service.CandidateEducationService;


@RestController
@CrossOrigin("*")
@RequestMapping("candidateeducation")
public class CandidateEducationController {
	
	
	@Autowired
	private CandidateEducationService candidateEducationService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addCandidateEudation(@RequestBody CandidateEducation candidateEducation){
		CandidateEducation addCandidateEducation = this.candidateEducationService.addCandidateEducation(candidateEducation);
		return new ResponseEntity<CandidateEducation>(addCandidateEducation, HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateCandidateEudation(@RequestBody CandidateEducation candidateEducation){
		CandidateEducation addCandidateEducation = this.candidateEducationService.updateCandidateEducation(candidateEducation);
		return new ResponseEntity<CandidateEducation>(addCandidateEducation, HttpStatus.OK);
	}
	
	@GetMapping("/get/list/by")
	public ResponseEntity<?> getCandidateEudation(
			@RequestParam("candidateId") long candidateId
			){
		List<CandidateEducation> listCandiateEducationByCandidate = this.candidateEducationService.getListCandiateEducationByCandidate(candidateId);
		return new ResponseEntity<List<CandidateEducation>>(listCandiateEducationByCandidate, HttpStatus.OK);
	}
	
	@GetMapping("/get/by")
	public ResponseEntity<?> getCandidateEudationById(
			@RequestParam("educationId") long educationId
			){
		CandidateEducation listCandiateEducationById = this.candidateEducationService.getListCandiateEducationById(educationId);
		return new ResponseEntity<CandidateEducation>(listCandiateEducationById, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/by")
	public ResponseEntity<?> delteCandidateEudation(
			@RequestParam("educationId") long educationId
			){
		this.candidateEducationService.deleteCandidateEducation(educationId);
		@SuppressWarnings("unchecked")
		Map<String, String> delted = new HashedMap();
		delted.put("status", "Candidate Education Deleted with id : "+educationId);
		return new ResponseEntity<Map<String, String>>(delted, HttpStatus.OK);
	}

}
