package com.tuespotsolutions.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.DistrictResponse;
import com.tuespotsolutions.service.DistrictService;

@RestController
@CrossOrigin("*")
@RequestMapping("/district")
public class DistrictController {
	
	Logger logger = LoggerFactory.getLogger(DistrictController.class);
	
	@Autowired
	private DistrictService districtService;
	
	@GetMapping("/by")
	public ResponseEntity<?> findByStateId(@RequestParam("stateId") Integer stateId){
		logger.info("line no : 31 findByStateId() method");
		List<DistrictResponse> findDistrictsByStateId = this.districtService.findDistrictsByStateId(stateId);
		return new ResponseEntity<List<DistrictResponse>>(findDistrictsByStateId, HttpStatus.OK);
	}

}
