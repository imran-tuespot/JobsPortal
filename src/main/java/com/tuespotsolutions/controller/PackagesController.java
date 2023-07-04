package com.tuespotsolutions.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.tuespotsolutions.models.PackagesRequest;
import com.tuespotsolutions.models.PackagesResponse;
import com.tuespotsolutions.models.PackagesResponseWithPagination;
import com.tuespotsolutions.service.PackagesService;

@RestController
@CrossOrigin("*")
@RequestMapping("/packages")
public class PackagesController {

	@Autowired
	private PackagesService packagesService;

	@PostMapping("/add")
	public ResponseEntity<?> savePackages(@Valid @RequestBody PackagesRequest packagesRequest) {
		PackagesResponse savePackages = this.packagesService.savePackages(packagesRequest);
		return new ResponseEntity<PackagesResponse>(savePackages, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<?> updatePackages(@Valid @RequestBody PackagesRequest packagesRequest) {
		PackagesResponse updatePackages = this.packagesService.updatePackages(packagesRequest);
		return new ResponseEntity<PackagesResponse>(updatePackages, HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<?> getPackages(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		PackagesResponseWithPagination findAllPackages = this.packagesService.findAllPackages(pageNumber, pageSize);
		return new ResponseEntity<PackagesResponseWithPagination>(findAllPackages, HttpStatus.OK);
	}

	@GetMapping("/by")
	public ResponseEntity<?> getPackagesById(@RequestParam(value = "packageId") Long packageId) {
		PackagesResponse findByPackagesId = this.packagesService.findByPackagesId(packageId);
		return new ResponseEntity<PackagesResponse>(findByPackagesId, HttpStatus.OK);
	}

	@DeleteMapping("/by")
	public ResponseEntity<?> deletePackageById(@RequestParam(value = "packageId") Long packageId) {
		this.packagesService.deletePackage(packageId);
		return new ResponseEntity<String>("Package Deleted SuccessFully", HttpStatus.OK);
	}

	@GetMapping("/company")
	public ResponseEntity<?> findPackagesByCompany(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		PackagesResponseWithPagination findPackagesByCompany = this.packagesService.findPackagesByCompany(pageNumber,
				pageSize);
		return new ResponseEntity<PackagesResponseWithPagination>(findPackagesByCompany, HttpStatus.OK);
	}

	@GetMapping("/candidate")
	public ResponseEntity<?> findPackagesByCandidate(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		PackagesResponseWithPagination findPackagesByCompany = this.packagesService.findPackagesByCandidate(pageNumber,
				pageSize);
		return new ResponseEntity<PackagesResponseWithPagination>(findPackagesByCompany, HttpStatus.OK);
	}

	@GetMapping("/get/company/for/website")
	public ResponseEntity<?> findPackagesOfCompanyForWesite(){
		List<PackagesResponse> findCompanyPackagesForWesite = this.packagesService.findCompanyPackagesForWesite();
		return new ResponseEntity<List<PackagesResponse>>(findCompanyPackagesForWesite, HttpStatus.OK);
	}
	
	@GetMapping("/get/candidate/for/website")
	public ResponseEntity<?> findPackagesOfCandidateWesite(){
		List<PackagesResponse> findCandidatePackagesForWesite = this.packagesService.findCandidatePackagesForWesite();
		return new ResponseEntity<List<PackagesResponse>>(findCandidatePackagesForWesite, HttpStatus.OK);
	}
	
	
}
