package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.AssignedPackageResponse;
import com.tuespotsolutions.models.AssignedPackagesRequest;
import com.tuespotsolutions.models.CandidateAssignedPackageList;
import com.tuespotsolutions.models.CompanyAssignedPackageList;
import com.tuespotsolutions.models.PackageWithActiveStatusListForCompany;
import com.tuespotsolutions.models.TransactionResponse;

public interface AssignedPackagesService {

	public AssignedPackageResponse assignedPackage(AssignedPackagesRequest assignedPackagesRequest);
	
	public List<PackageWithActiveStatusListForCompany> getCompanyPackageList(Long userId);
	
	public List<PackageWithActiveStatusListForCompany> getCandidatePackageList(Long userId);
	
	public AssignedPackageResponse updateAssignedPackage(Long assignedPackageId);
	
	public List<CompanyAssignedPackageList> getCompmanyAssignedPackageList();
	
	public List<CandidateAssignedPackageList> getCandidateAssignedPackageList();
	
}
