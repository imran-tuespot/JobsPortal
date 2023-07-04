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

import com.tuespotsolutions.models.PackageDiscriptionModel;
import com.tuespotsolutions.models.PackageDiscriptionPagination;
import com.tuespotsolutions.models.PackageDiscriptionResponse;
import com.tuespotsolutions.service.PackageDiscriptionService;

@RestController
@CrossOrigin("*")
@RequestMapping("/packagediscription")
public class PackageDiscriptionController {

	@Autowired
	private PackageDiscriptionService packageDiscriptionService;

	@PostMapping("/add")
	public ResponseEntity<?> postPackageDiscription(@RequestBody List<PackageDiscriptionModel> discriptionModel) {
	 List<PackageDiscriptionResponse> addDescription = this.packageDiscriptionService.addDescription(discriptionModel);
		return new ResponseEntity<List<PackageDiscriptionResponse>>(addDescription, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<?> updatePackageDiscription(@RequestBody PackageDiscriptionModel discriptionModel) {
		PackageDiscriptionResponse addDescription = this.packageDiscriptionService.updateDescription(discriptionModel);
		return new ResponseEntity<PackageDiscriptionResponse>(addDescription, HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<?> findByPakckageId(@RequestParam("packageId") Long packageId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		PackageDiscriptionPagination findByPackageId = this.packageDiscriptionService.findByPackageId(packageId,
				pageNumber, pageSize);
		return new ResponseEntity<PackageDiscriptionPagination>(findByPackageId, HttpStatus.OK);
	}

	@GetMapping("/by")
	public ResponseEntity<?> findByPakckageId(@RequestParam("descriptionId") Long descriptionId) {
		PackageDiscriptionResponse findByDescriptionId = this.packageDiscriptionService
				.findByDescriptionId(descriptionId);
		return new ResponseEntity<PackageDiscriptionResponse>(findByDescriptionId, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(@RequestParam("descriptionId") Long descriptionId) {
		this.packageDiscriptionService.deleteDescription(descriptionId);
		Map<String, String> resp = new HashMap<String, String>();
		resp.put("status", "Discription Deleted SuccessFully");
		return new ResponseEntity<Map<String, String>>(resp, HttpStatus.OK);
	}
	

}
