package com.tuespotsolutions.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.JobAppliedRequest;
import com.tuespotsolutions.service.JobAppliedService;

@RestController
@CrossOrigin("*")
@RequestMapping("/apply")
public class JobAppliedController {
	
	@Autowired
	private JobAppliedService jobAppliedService;

	@PostMapping("/job")
	public ResponseEntity<?> applyJob(@RequestBody JobAppliedRequest jobAppliedRequest){
		Map<String, String> jobApply = this.jobAppliedService.jobApply(jobAppliedRequest);
		return new ResponseEntity<Map<String, String>>(jobApply, HttpStatus.CREATED);
	}
	
}
