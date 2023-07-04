package com.tuespotsolutions.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.JobDescriptionRequest;
import com.tuespotsolutions.models.JobDescriptionResponse;
import com.tuespotsolutions.service.JobDescriptionService;

@RestController
@CrossOrigin("*")
@RequestMapping("/jobdescription")
public class JobDescriptionController {

	@Autowired
	private JobDescriptionService jobDescriptionService;
	
	
	@PostMapping("/add")
	public ResponseEntity<?> addJobDescription(@RequestBody List<JobDescriptionRequest> jobDescriptionRequests){
		List<JobDescriptionResponse> addDescription = this.jobDescriptionService.addDescription(jobDescriptionRequests);
		return new ResponseEntity<List<JobDescriptionResponse>>(addDescription, HttpStatus.OK);
	}
	
	public ResponseEntity<?> getJobDescriptionWithJobId(
			@RequestParam("jobId") Long jobId
			){
		List<JobDescriptionResponse> descriptionWithJobId = this.jobDescriptionService.getDescriptionWithJobId(jobId);
		return new ResponseEntity<List<JobDescriptionResponse>>(descriptionWithJobId, HttpStatus.OK);
	}
	
}
