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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.FilterValueResponse;
import com.tuespotsolutions.models.FilterValueResponseWithPagination;
import com.tuespotsolutions.models.FilterValuesRequest;
import com.tuespotsolutions.service.FilterValueService;


@RestController
@CrossOrigin("*")
@RequestMapping("/filtervalue")
public class FilterValueController {

	@Autowired
	private FilterValueService filterValueService;

	@PostMapping("/add")
	public ResponseEntity<?> addFilterValues(@RequestBody List<FilterValuesRequest> filterValuesRequests) {
		List<FilterValueResponse> addFilterValue = this.filterValueService.addFilterValue(filterValuesRequests);
		return new ResponseEntity<List<FilterValueResponse>>(addFilterValue, HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateFilterValues(@RequestBody FilterValuesRequest filterValuesRequests) {
		FilterValueResponse updateFilterValue = this.filterValueService.updateFilterValue(filterValuesRequests);
		return new ResponseEntity<FilterValueResponse>(updateFilterValue, HttpStatus.CREATED);
	}
	
	@GetMapping("/get/all/by")
	public ResponseEntity<?> getFiltersValue(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
			) {
		FilterValueResponseWithPagination filterList = this.filterValueService.getFilterList(pageNumber, pageSize);
		return new ResponseEntity<FilterValueResponseWithPagination>(filterList, HttpStatus.OK);
	}
	
	
	@GetMapping("/get/by")
	public ResponseEntity<?> getFilterValuesById(
			@RequestParam(value = "id") Long id
			) {
		FilterValueResponse filterById = this.filterValueService.getFilterById(id);
		return new ResponseEntity<FilterValueResponse>(filterById, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/by")
	public ResponseEntity<?> deleteFilterValue(@RequestParam("id") Long id){
		this.filterValueService.deleteFilterValue(id);
		Map<String, String> resp = new HashMap<String, String>();
		resp.put("status", "Filter Value Deleted");
		return new ResponseEntity<Map<String, String>>(resp,HttpStatus.OK);
		
	}

}
