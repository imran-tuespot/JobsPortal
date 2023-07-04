package com.tuespotsolutions.service;


import java.util.List;

import com.tuespotsolutions.models.PackageDiscriptionModel;
import com.tuespotsolutions.models.PackageDiscriptionPagination;
import com.tuespotsolutions.models.PackageDiscriptionResponse;

public interface PackageDiscriptionService {

	public List<PackageDiscriptionResponse> addDescription(List<PackageDiscriptionModel> discription);
	
	public PackageDiscriptionResponse updateDescription(PackageDiscriptionModel discription);
	
	public PackageDiscriptionPagination findByPackageId(long packageId, int pageNumber, int pageSize);
	
	public PackageDiscriptionResponse findByDescriptionId(long descriptionId);
	
	public void deleteDescription(long descriptionId);
	
}
