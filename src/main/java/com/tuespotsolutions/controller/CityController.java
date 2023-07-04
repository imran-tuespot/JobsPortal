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

import com.tuespotsolutions.models.CityResponse;
import com.tuespotsolutions.service.CityService;

@RestController
@CrossOrigin("*")
@RequestMapping("/city")
public class CityController {
	
	Logger logger = LoggerFactory.getLogger(CityController.class);

	@Autowired
	private CityService cityService;

	@GetMapping("/by")
	public ResponseEntity<?> findCityListByDistrictId(@RequestParam("districtId") Integer distrcitId) {
		logger.info("line no : 31 registerCandidate() method");
		List<CityResponse> cityList = this.cityService.findCityByDistrictId(distrcitId);
		return new ResponseEntity<List<CityResponse>>(cityList, HttpStatus.OK);
	}

}
