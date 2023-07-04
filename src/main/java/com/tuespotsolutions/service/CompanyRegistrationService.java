package com.tuespotsolutions.service;

import java.util.Map;

import com.tuespotsolutions.models.CompanyDetailResponse;
import com.tuespotsolutions.models.CompanyListWithPagination;
import com.tuespotsolutions.models.CompanyProfileRequest;
import com.tuespotsolutions.models.CompanyProfileResponse;
import com.tuespotsolutions.models.CompanyRequest;
import com.tuespotsolutions.models.UpdateCompanyInterViewLink;

public interface CompanyRegistrationService {
	
	// company register
	public Map<String, String> registerCompany(CompanyRequest companyRequest);
	public CompanyProfileResponse getCompanyProfileDetails(long companyId);
	public Map<String, String> updateCompanyProfile(CompanyProfileRequest companyProfileResponse);
	public CompanyListWithPagination getCompanyList(Integer page, Integer size);
	public CompanyDetailResponse getCompanyDetailById(long companyId);
	public Map<String, String> updateInterViewLink(UpdateCompanyInterViewLink companyInterViewLink); 
	
}
