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

import com.tuespotsolutions.models.StateResponse;
import com.tuespotsolutions.service.StateService;

@RestController
@CrossOrigin("*")
@RequestMapping("/state")
public class StateController {
	
	Logger logger = LoggerFactory.getLogger(StateController.class);
	
	@Autowired
	private StateService stateService;
	
	@GetMapping("/all")
	public ResponseEntity<?> findAllStates(){
		logger.info("line no : 31 findAllStates() method");
		List<StateResponse> statesResponse = this.stateService.findStates();
		return new ResponseEntity<List<StateResponse>>(statesResponse, HttpStatus.OK);
	}
	
	@GetMapping("/by")
	public ResponseEntity<?> findStateById(@RequestParam("stateId") Integer stateId){
		logger.info("line no : 38 findStateById() method");
		StateResponse stateResponse = this.stateService.findStateById(stateId);
		return new ResponseEntity<StateResponse>(stateResponse, HttpStatus.OK);
	}

}
