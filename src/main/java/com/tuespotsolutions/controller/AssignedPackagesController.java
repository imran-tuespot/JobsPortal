package com.tuespotsolutions.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.AssignedPackageResponse;
import com.tuespotsolutions.models.AssignedPackagesRequest;
import com.tuespotsolutions.models.CandidateAssignedPackageList;
import com.tuespotsolutions.models.CompanyAssignedPackageList;
import com.tuespotsolutions.models.PackageWithActiveStatusListForCompany;
import com.tuespotsolutions.service.AssignedPackagesService;

@RestController
@CrossOrigin("*")
@RequestMapping("/assigned")
public class AssignedPackagesController {

	@Autowired
	private AssignedPackagesService assignedPackagesService;

	@PostMapping("/packages")
	public ResponseEntity<?> assignedPackage(@RequestBody AssignedPackagesRequest assignedPackagesRequest) {
		AssignedPackageResponse assignedPackage = this.assignedPackagesService.assignedPackage(assignedPackagesRequest);
		return new ResponseEntity<AssignedPackageResponse>(assignedPackage, HttpStatus.CREATED);
	}

	@GetMapping("/company/package/get/by")
	public ResponseEntity<?> getAssignedCompanyPackagesByUserId(@RequestParam("userId") Long userId) {
		List<PackageWithActiveStatusListForCompany> companyPackageList = this.assignedPackagesService
				.getCompanyPackageList(userId);
		return new ResponseEntity<List<PackageWithActiveStatusListForCompany>>(companyPackageList, HttpStatus.OK);
	}

	@GetMapping("/candidate/package/get/by")
	public ResponseEntity<?> getAssignedCandidatePackagesByUserId(@RequestParam("userId") Long userId) {
		List<PackageWithActiveStatusListForCompany> companyPackageList = this.assignedPackagesService
				.getCandidatePackageList(userId);
		return new ResponseEntity<List<PackageWithActiveStatusListForCompany>>(companyPackageList, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateAssignedPackage(@RequestParam("assignedPackageId") Long assignedPackageId) {
		AssignedPackageResponse updateAssignedPackage = this.assignedPackagesService
				.updateAssignedPackage(assignedPackageId);
		return new ResponseEntity<AssignedPackageResponse>(updateAssignedPackage, HttpStatus.OK);
	}

	@GetMapping("/get/company/assigned/packages/detail/list")
	public ResponseEntity<?> getCompanyAssignedPackagesList() {
		List<CompanyAssignedPackageList> compmanyAssignedPackageList = this.assignedPackagesService
				.getCompmanyAssignedPackageList();
		return new ResponseEntity<List<CompanyAssignedPackageList>>(compmanyAssignedPackageList, HttpStatus.OK);
	}

	@GetMapping("/get/candidate/assigned/packages/detail/list")
	public ResponseEntity<?> getCandidateAssignedPackagesList() {
		List<CandidateAssignedPackageList> candidateAssignedPackageList = this.assignedPackagesService
				.getCandidateAssignedPackageList();
		return new ResponseEntity<List<CandidateAssignedPackageList>>(candidateAssignedPackageList, HttpStatus.OK);
	}

}
