package com.tuespotsolutions.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.CandidateSkillsRequest;
import com.tuespotsolutions.service.CandidateSkillsService;

@RestController
@CrossOrigin("*")
@RequestMapping("/candidateskill")
public class CandidateSkillsController {

	@Autowired
	CandidateSkillsService candidateSkillsService;

	@PostMapping("/add")
	public ResponseEntity<?> addSkills(@RequestBody List<CandidateSkillsRequest> candidateSkillsRequests) {
		Map<String, String> addSillDetail = this.candidateSkillsService.addSillDetail(candidateSkillsRequests);
		return new ResponseEntity<Map<String, String>>(addSillDetail, HttpStatus.OK);
	}

	@GetMapping("/get/all")
	public ResponseEntity<?> getAllSkills() {
		List<CandidateSkillsRequest> skillDetailList = this.candidateSkillsService.getSkillDetailList();
		return new ResponseEntity<List<CandidateSkillsRequest>>(skillDetailList, HttpStatus.OK);
	}

	@GetMapping("/get/by")
	public ResponseEntity<?> getSkillsByCandidateId(@RequestParam("candidateId") Long candidateId) {
		List<CandidateSkillsRequest> skillDetailList = this.candidateSkillsService
				.getSkillDetailByCandidateId(candidateId);
		return new ResponseEntity<List<CandidateSkillsRequest>>(skillDetailList, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteSkills(@RequestParam("skillId") Long skillId) {
		this.candidateSkillsService.deleteSalaryDetail(skillId);
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "Skill Deleted");
		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
	}
}
