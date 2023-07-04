package com.tuespotsolutions.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.JobWorkModeModel;
import com.tuespotsolutions.service.JobWorkModeService;

@RestController
@CrossOrigin("*")
@RequestMapping("/jobworkmode")
public class JobWorkModeController {

	@Autowired 
	private JobWorkModeService jobWorkModeService;
	
	@GetMapping("/get/list")
	public ResponseEntity<?> getWorkModeList(){
		List<JobWorkModeModel> jobWorkModeList = this.jobWorkModeService.getJobWorkModeList();
		return new ResponseEntity<List<JobWorkModeModel>>(jobWorkModeList, HttpStatus.OK);
	}
}
