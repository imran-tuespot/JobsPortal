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

import com.tuespotsolutions.models.CandidateLanguage;
import com.tuespotsolutions.service.CandidateLanguageService;

@RestController
@CrossOrigin("*")
@RequestMapping("/language")
public class CandidateLanguageController {

	@Autowired
	private CandidateLanguageService candidateLanguageService;

	@PostMapping("/add")
	public ResponseEntity<?> addLanguage(@RequestBody CandidateLanguage candidateLanguage) {
		CandidateLanguage addLanguage = this.candidateLanguageService.addLanguage(candidateLanguage);
		return new ResponseEntity<CandidateLanguage>(addLanguage, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateLanguage(@RequestBody CandidateLanguage candidateLanguage) {
		CandidateLanguage addLanguage = this.candidateLanguageService.updateLanguage(candidateLanguage);
		return new ResponseEntity<CandidateLanguage>(addLanguage, HttpStatus.OK);
	}

	@GetMapping("/get/list/by")
	public ResponseEntity<?> getLanguageList(@RequestParam("candidateId") long candidateId) {
		List<CandidateLanguage> languageListByCandidateId = this.candidateLanguageService
				.getLanguageListByCandidateId(candidateId);
		return new ResponseEntity<List<CandidateLanguage>>(languageListByCandidateId, HttpStatus.OK);
	}

	@GetMapping("/get/by")
	public ResponseEntity<?> getLanguage(@RequestParam("languageId") long languageId) {
		CandidateLanguage languageByLanguageId = this.candidateLanguageService.getLanguageByLanguageId(languageId);
		return new ResponseEntity<CandidateLanguage>(languageByLanguageId, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/by")
	public ResponseEntity<?> deleteLanguage(@RequestParam("languageId") long languageId) {
		this.candidateLanguageService.deleteLanguageByLanguageId(languageId);
		@SuppressWarnings("unchecked")
		Map<String, String> delted = new HashedMap();
		delted.put("status", "Candidate Education Deleted with id : "+languageId);
		return new ResponseEntity<Map<String, String>>(delted, HttpStatus.OK);
	}
}
