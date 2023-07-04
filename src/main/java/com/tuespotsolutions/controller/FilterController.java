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

import com.tuespotsolutions.models.FilteredJobsWithFilters;
import com.tuespotsolutions.models.FiltersList;
import com.tuespotsolutions.models.FiltersListForJobPost;
import com.tuespotsolutions.models.FiltersRequest;
import com.tuespotsolutions.models.FiltersRequestWithPagination;
import com.tuespotsolutions.models.JobFilterModel;
import com.tuespotsolutions.models.JobLocationsGroup;
import com.tuespotsolutions.models.JobResponseWithPagination;
import com.tuespotsolutions.models.JobWorkModeModel;
import com.tuespotsolutions.models.PostedOnJob;
import com.tuespotsolutions.service.FilterService;

@RestController
@CrossOrigin("*")
@RequestMapping("/filter")
public class FilterController {

	@Autowired
	private FilterService filterService;

	@PostMapping("/add")
	public ResponseEntity<?> addFilters(@RequestBody FiltersRequest filtersRequest) {
		FiltersRequest addFilters = this.filterService.addFilters(filtersRequest);
		return new ResponseEntity<FiltersRequest>(addFilters, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateFilters(@RequestBody FiltersRequest filtersRequest) {
		FiltersRequest addFilters = this.filterService.updateFilters(filtersRequest);
		return new ResponseEntity<FiltersRequest>(addFilters, HttpStatus.OK);
	}

	@GetMapping("/get/all")
	public ResponseEntity<?> getAllFilters(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		FiltersRequestWithPagination filters = this.filterService.getFilters(pageNumber, pageSize);
		return new ResponseEntity<FiltersRequestWithPagination>(filters, HttpStatus.CREATED);
	}

	@GetMapping("/get/by")
	public ResponseEntity<?> getFilterById(@RequestParam("filterId") Long filterId) {
		FiltersRequest filterById = this.filterService.getFilterById(filterId);
		return new ResponseEntity<FiltersRequest>(filterById, HttpStatus.OK);
	}

	@DeleteMapping("/delete/by")
	public ResponseEntity<?> deleteFilter(@RequestParam("filterId") Long filterId) {
		this.filterService.deleteFilter(filterId);
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "Filter has been deleted");
		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
	}

	@GetMapping("/get/location/filter/by")
	public ResponseEntity<?> getLocationFilter(@RequestParam("jobTitle") String jobTitle) {
		List<JobLocationsGroup> findJobLoactionGroup = this.filterService.findJobLoactionGroup(jobTitle);
		return new ResponseEntity<List<JobLocationsGroup>>(findJobLoactionGroup, HttpStatus.OK);
	}

	@GetMapping("/get/enabled/filters")
	public ResponseEntity<?> getEnabledFilterList() {
		List<FiltersRequest> enabledFilters = this.filterService.getEnabledFilters();
		return new ResponseEntity<List<FiltersRequest>>(enabledFilters, HttpStatus.OK);
	}

	@PostMapping("/get/job/with/locationfilter/by")
	public ResponseEntity<?> getJobWithByLocationFilter(@RequestBody List<JobLocationsGroup> JobLocationsGroup,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		FilteredJobsWithFilters filterJobsByCity = this.filterService.filterJobsByCity(JobLocationsGroup, pageNumber,
				pageSize);
		return new ResponseEntity<FilteredJobsWithFilters>(filterJobsByCity, HttpStatus.OK);
	}

	@GetMapping("/get/postedon/filters")
	public ResponseEntity<?> getPostedOnJobFilter(@RequestParam(value = "jobTitle", required = false) String jobTitle) {
		List<PostedOnJob> findPostedOnGroup = this.filterService.findPostedOnGroup(jobTitle);
		return new ResponseEntity<List<PostedOnJob>>(findPostedOnGroup, HttpStatus.OK);
	}

	@GetMapping("/get/workmode/filters")
	public ResponseEntity<?> getWorkModeFilterList(
			@RequestParam(value = "jobTitle", required = false) String jobTitle) {
		List<JobWorkModeModel> workModeFilterList = this.filterService.workModeFilterList(jobTitle);
		return new ResponseEntity<List<JobWorkModeModel>>(workModeFilterList, HttpStatus.OK);
	}

	@PostMapping("/get/postedon/job/list")
	public ResponseEntity<?> getJobListWithPostedOnFilter(@RequestBody List<PostedOnJob> postedOnJobs,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		FilteredJobsWithFilters filterJobsByPostedOn = this.filterService.filterJobsByPostedOn(postedOnJobs, pageNumber,
				pageSize);
		return new ResponseEntity<FilteredJobsWithFilters>(filterJobsByPostedOn, HttpStatus.OK);
	}

	@PostMapping("/get/by/workmode/job/list")
	public ResponseEntity<?> getJobListWithWorkModeFilter(@RequestBody List<JobWorkModeModel> jobWorkModeModels,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		FilteredJobsWithFilters filterJobsByPostedOn = this.filterService.getJobWithWorkModeFilter(jobWorkModeModels,
				pageNumber, pageSize);
		return new ResponseEntity<FilteredJobsWithFilters>(filterJobsByPostedOn, HttpStatus.OK);
	}

	@GetMapping("/filterlist/with/values")
	public ResponseEntity<?> getFilterListWithValues() {
		List<FiltersListForJobPost> findAllFilterListWithValues = this.filterService.findAllFilterListWithValues();
		return new ResponseEntity<List<FiltersListForJobPost>>(findAllFilterListWithValues, HttpStatus.OK);
	}
	
	
	@GetMapping("/filterlist/with/jobcount/by")
	public ResponseEntity<?> getFilterCount(@RequestParam(value="jobTitle", required = false) String jobTitle,
			@RequestParam(value="jobLocation", required = false) String jobLocation
			){
		 List<FiltersList> filterList = this.filterService.getFilterList(jobTitle,jobLocation);
		return new ResponseEntity<List<FiltersList>>(filterList, HttpStatus.OK);
	}

	
	@PostMapping("/list/by/checked/filters")
	public ResponseEntity<?> getJobListByCheckedFilters(@RequestBody List<FiltersList> filtersList,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
			){
		JobResponseWithPagination jobListByCheckedFilters = this.filterService.getJobListByCheckedFilters(filtersList, pageNumber, pageSize);
		return new ResponseEntity<JobResponseWithPagination>(jobListByCheckedFilters, HttpStatus.OK);
	}
	
}
