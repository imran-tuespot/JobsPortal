package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.PackagesResponse;
import com.tuespotsolutions.models.PackagesResponseWithPagination;

public interface WebsitePackagesService {

	public List<PackagesResponse> findPackagesByCompany();
	
	public List<PackagesResponse> findPackagesByCandidate();
	
}
