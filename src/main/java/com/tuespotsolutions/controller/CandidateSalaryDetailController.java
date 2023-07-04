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

import com.tuespotsolutions.models.CandidateSalaryDetail;
import com.tuespotsolutions.service.CandidateSalaryDetailService;

@RestController
@CrossOrigin("*")
@RequestMapping("/salarydetail")
public class CandidateSalaryDetailController {
	
	@Autowired
	private CandidateSalaryDetailService candidateSalaryDetailService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addSalaryDetail(@RequestBody CandidateSalaryDetail candidateSalaryDetail){
		CandidateSalaryDetail addSalaryDetail = this.candidateSalaryDetailService.addSalaryDetail(candidateSalaryDetail);
		return new ResponseEntity<CandidateSalaryDetail>(addSalaryDetail, HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateSalaryDetail(@RequestBody CandidateSalaryDetail candidateSalaryDetail){
		CandidateSalaryDetail addSalaryDetail = this.candidateSalaryDetailService.updateSalaryDetail(candidateSalaryDetail);
		return new ResponseEntity<CandidateSalaryDetail>(addSalaryDetail, HttpStatus.OK);
	}
	
	@GetMapping("/get/by")
	public ResponseEntity<?> getSalaryDetailByCandidateId(
			@RequestParam("candidateId") long candidateId
			){
		List<CandidateSalaryDetail> salaryDetailList = this.candidateSalaryDetailService.getSalaryDetailList(candidateId);
		return new ResponseEntity<List<CandidateSalaryDetail>>(salaryDetailList, HttpStatus.OK);
	}
	
	@GetMapping("/candidate/get/by")
	public ResponseEntity<?> getSalaryDetailBySalaryId(
			@RequestParam("salaryId") long salaryId
			){
	    CandidateSalaryDetail salaryDetailById = this.candidateSalaryDetailService.getSalaryDetailById(salaryId);
		return new ResponseEntity<CandidateSalaryDetail>(salaryDetailById, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/by")
	public ResponseEntity<?> deleteSalaryDetailBySalaryId(
			@RequestParam("salaryId") long salaryId
			){
	   this.candidateSalaryDetailService.deleteSalaryDetail(salaryId);
	   @SuppressWarnings("unchecked")
		Map<String, String> delted = new HashedMap();
		delted.put("status", "Candidate Salary Detail Deleted with id : "+salaryId);
		return new ResponseEntity<Map<String, String>>(delted, HttpStatus.OK);
	   
	}
}
