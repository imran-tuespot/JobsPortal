package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.PackageWithActiveStatusListForCompany;
import com.tuespotsolutions.models.PackagesRequest;
import com.tuespotsolutions.models.PackagesResponse;
import com.tuespotsolutions.models.PackagesResponseWithPagination;

public interface PackagesService {

	public PackagesResponse savePackages(PackagesRequest packagesRequest);
	
	public PackagesResponse updatePackages(PackagesRequest packagesRequest);
	
	public PackagesResponseWithPagination findAllPackages(int page, int size);
	
	public PackagesResponse findByPackagesId(Long packageId);
	
	public void deletePackage(Long packageId);
	
	public PackagesResponseWithPagination findPackagesByCompany(int page, int size);
	
	public PackagesResponseWithPagination findPackagesByCandidate(int page, int size);
	
	public List<PackagesResponse> findCompanyPackagesForWesite(); 
	
	public List<PackagesResponse> findCandidatePackagesForWesite();
		
}
