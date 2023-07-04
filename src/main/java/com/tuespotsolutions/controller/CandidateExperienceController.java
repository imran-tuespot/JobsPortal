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

import com.tuespotsolutions.models.CandidateExperience;
import com.tuespotsolutions.service.CandidateExperienceService;

@RestController
@CrossOrigin("*")
@RequestMapping("/candidateexperience")
public class CandidateExperienceController {

	@Autowired
	private CandidateExperienceService candidateExperienceService;

	@PostMapping("/add")
	public ResponseEntity<?> addCanidateExperience(@RequestBody CandidateExperience candidateExperience) {
		CandidateExperience addCanidateExperience = this.candidateExperienceService
				.addCanidateExperience(candidateExperience);
		return new ResponseEntity<CandidateExperience>(addCanidateExperience, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateCanidateExperience(@RequestBody CandidateExperience candidateExperience) {
		CandidateExperience addCanidateExperience = this.candidateExperienceService
				.updateCanidateExperience(candidateExperience);
		return new ResponseEntity<CandidateExperience>(addCanidateExperience, HttpStatus.OK);
	}

	@GetMapping("/get/list/by")
	public ResponseEntity<?> getCanidateExperienceList(@RequestParam("candidateId") long candidateId) {
		List<CandidateExperience> candidateExperience = this.candidateExperienceService
				.getCandidateExperience(candidateId);
		return new ResponseEntity<List<CandidateExperience>>(candidateExperience, HttpStatus.OK);
	}

	@GetMapping("/get/by")
	public ResponseEntity<?> getCanidateExperienceById(@RequestParam("experienceId") long experienceId) {
		CandidateExperience candidateExperienceByExperienceId = this.candidateExperienceService
				.getCandidateExperienceByExperienceId(experienceId);
		return new ResponseEntity<CandidateExperience>(candidateExperienceByExperienceId, HttpStatus.OK);
	}

	@DeleteMapping("/delete/by")
	public ResponseEntity<?> deleteCanidateExperience(@RequestParam("expericeId") long expericeId) {
		this.candidateExperienceService.deleteCandidateExperience(expericeId);
		@SuppressWarnings("unchecked")
		Map<String, String> delted = new HashedMap();
		delted.put("status", "Candidate Experience Deleted with id : " + expericeId);
		return new ResponseEntity<Map<String, String>>(delted, HttpStatus.OK);
	}
}
