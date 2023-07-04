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

import com.tuespotsolutions.models.CandidateProjectDetail;
import com.tuespotsolutions.service.CandidateProjectDetailService;

@RestController
@CrossOrigin("*")
@RequestMapping("/candidateprojectdetail")
public class CandidateProjectDetailController {

	@Autowired
	private CandidateProjectDetailService candidateProjectDetailService;

	@PostMapping("/add")
	public ResponseEntity<?> addCanidateProjectDatil(@RequestBody CandidateProjectDetail candidateProjectDetail) {
		CandidateProjectDetail addCandidateProjectDetail = this.candidateProjectDetailService
				.addCandidateProjectDetail(candidateProjectDetail);
		return new ResponseEntity<CandidateProjectDetail>(addCandidateProjectDetail, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateCanidateProjectDatil(@RequestBody CandidateProjectDetail candidateProjectDetail) {
		CandidateProjectDetail addCandidateProjectDetail = this.candidateProjectDetailService
				.updateCandidateProjectDetail(candidateProjectDetail);
		return new ResponseEntity<CandidateProjectDetail>(addCandidateProjectDetail, HttpStatus.CREATED);
	}

	@GetMapping("/get/by")
	public ResponseEntity<?> getCandidateProjectDetailList(@RequestParam("candidateId") long candidateId) {
		List<CandidateProjectDetail> candidateProjectDetailList = this.candidateProjectDetailService
				.getCandidateProjectDetailList(candidateId);
		return new ResponseEntity<List<CandidateProjectDetail>>(candidateProjectDetailList, HttpStatus.OK);
	}

	@GetMapping("/get/projectdetail/by")
	public ResponseEntity<?> getCandidateProjectDetail(@RequestParam("projectId") long projectId) {
        CandidateProjectDetail candidateProjectDetailById = this.candidateProjectDetailService.getCandidateProjectDetailById(projectId);
		return new ResponseEntity<CandidateProjectDetail>(candidateProjectDetailById, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/by")
	public ResponseEntity<?> deleteCandidateProjectDetail(@RequestParam("projectId") long projectId) {
       this.candidateProjectDetailService.deleteCandidateProjectDetail(projectId);
       @SuppressWarnings("unchecked")
		Map<String, String> delted = new HashedMap();
		delted.put("status", "Candidate Education Deleted with id : "+projectId);
		return new ResponseEntity<Map<String, String>>(delted, HttpStatus.OK);
	}
}
