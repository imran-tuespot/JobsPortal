package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.entity.PackageDiscription;
import com.tuespotsolutions.entity.Packages;
import com.tuespotsolutions.models.PackageDiscriptionResponse;
import com.tuespotsolutions.models.PackagesResponse;
import com.tuespotsolutions.repository.PackagesRepository;
import com.tuespotsolutions.service.WebsitePackagesService;

@Service
public class WebsitePackagesServiceImpl implements WebsitePackagesService{
	
	@Autowired
	private PackagesRepository packagesRepository;

	@Override
	public List<PackagesResponse> findPackagesByCompany() {
		
		List<Packages> findByCompany = this.packagesRepository.findByCompany(true);
		List<PackagesResponse> companyPackagesList = new ArrayList<PackagesResponse>();
		findByCompany.forEach(company->{
			PackagesResponse packagesResponse = new PackagesResponse();
			packagesResponse.setCandidate(company.isCandidate());
			packagesResponse.setCompany(company.isCompany());
			packagesResponse.setDays(company.getDays());
			packagesResponse.setDiscount(company.getDiscount());
			packagesResponse.setId(company.getId());
			packagesResponse.setName(company.getName());
			packagesResponse.setPrice(company.getPrice());
			packagesResponse.setType(company.getType());
			List<PackageDiscription> packageDiscriptions = company.getPackageDiscriptions();
			if (!packageDiscriptions.isEmpty()) {
				List<PackageDiscriptionResponse> discriptionResponses = new ArrayList<PackageDiscriptionResponse>();
				packageDiscriptions.forEach(disc -> {
					PackageDiscriptionResponse discriptionResponse = new PackageDiscriptionResponse();
					discriptionResponse.setId(disc.getId());
					discriptionResponse.setDescription(disc.getDescription());
					discriptionResponse.setPackageId(disc.getPackages().getId());
					discriptionResponses.add(discriptionResponse);
				});
				packagesResponse.setDiscription(discriptionResponses);
			}
			companyPackagesList.add(packagesResponse);
		});
		
		return companyPackagesList;
	}

	@Override
	public List<PackagesResponse> findPackagesByCandidate() {
		List<Packages> findByCandidate = this.packagesRepository.findByCandidate(true);
		List<PackagesResponse> candidatePackageList = new ArrayList<PackagesResponse>();
		findByCandidate.forEach(candidate->{
			PackagesResponse packagesResponse = new PackagesResponse();
			packagesResponse.setCandidate(candidate.isCandidate());
			packagesResponse.setCompany(candidate.isCompany());
			packagesResponse.setDays(candidate.getDays());
			packagesResponse.setDiscount(candidate.getDiscount());
			packagesResponse.setId(candidate.getId());
			packagesResponse.setName(candidate.getName());
			packagesResponse.setPrice(candidate.getPrice());
			packagesResponse.setType(candidate.getType());
			List<PackageDiscription> packageDiscriptions = candidate.getPackageDiscriptions();
			if (!packageDiscriptions.isEmpty()) {
				List<PackageDiscriptionResponse> discriptionResponses = new ArrayList<PackageDiscriptionResponse>();
				packageDiscriptions.forEach(disc -> {
					PackageDiscriptionResponse discriptionResponse = new PackageDiscriptionResponse();
					discriptionResponse.setId(disc.getId());
					discriptionResponse.setDescription(disc.getDescription());
					discriptionResponse.setPackageId(disc.getPackages().getId());
					discriptionResponses.add(discriptionResponse);
				});
				packagesResponse.setDiscription(discriptionResponses);
			}
			candidatePackageList.add(packagesResponse);
		});
		
		return candidatePackageList;
	}

}
