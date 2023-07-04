package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.PackageDiscription;
import com.tuespotsolutions.entity.Packages;
import com.tuespotsolutions.models.PackageDiscriptionModel;
import com.tuespotsolutions.models.PackageDiscriptionPagination;
import com.tuespotsolutions.models.PackageDiscriptionResponse;
import com.tuespotsolutions.repository.PackageDiscriptionRepository;
import com.tuespotsolutions.repository.PackagesRepository;
import com.tuespotsolutions.service.PackageDiscriptionService;

@Service
public class PackageDiscriptionServiceImpl implements PackageDiscriptionService {

	@Autowired
	PackagesRepository packagesRepository;

	@Autowired
	PackageDiscriptionRepository discriptionRepository;

	@Override
	public List<PackageDiscriptionResponse> addDescription(List<PackageDiscriptionModel> discription) {
		
		List<PackageDiscription> packageDescription = new ArrayList<PackageDiscription>();
		discription.forEach(data->{
			PackageDiscription packageDiscription = new PackageDiscription();
			packageDiscription.setDescription(data.getDescription());
			Packages packages = this.packagesRepository.findById(data.getPackageId()).orElseThrow(() -> new ResourceNotFoundException(
					"Package Not Found with package id : " + data.getPackageId()));
			packageDiscription.setPackages(packages);
			packageDescription.add(packageDiscription);
		});
		
		List<PackageDiscription> saveAll = this.discriptionRepository.saveAll(packageDescription);
		
		List<PackageDiscriptionResponse> discriptionResponseList = new ArrayList<PackageDiscriptionResponse>();
		saveAll.forEach(data->{
			PackageDiscriptionResponse discriptionResponse = new PackageDiscriptionResponse();
			discriptionResponse.setId(data.getId());
			discriptionResponse.setDescription(data.getDescription());
			discriptionResponseList.add(discriptionResponse);
		});
		
		//PackageDiscription save = this.discriptionRepository.save(packageDiscription);
		
		
		
		return discriptionResponseList;
	}

	@Override
	public PackageDiscriptionResponse updateDescription(PackageDiscriptionModel discription) {
		PackageDiscription packageDiscription = new PackageDiscription();
		packageDiscription.setDescription(discription.getDescription());

		Packages packages = this.packagesRepository.findById(discription.getPackageId()).orElseThrow(() -> new ResourceNotFoundException(
				"Package Not Found with package id : " + discription.getPackageId()));

		packageDiscription.setId(discription.getId());
		packageDiscription.setPackages(packages);
		
		PackageDiscription save = this.discriptionRepository.save(packageDiscription);
		
		PackageDiscriptionResponse discriptionResponse = new PackageDiscriptionResponse();
		discriptionResponse.setId(save.getId());
		discriptionResponse.setDescription(save.getDescription());
		
		return discriptionResponse;
	}

	@Override
	public PackageDiscriptionPagination  findByPackageId(long packageId, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Packages packages = this.packagesRepository.findById(packageId).orElseThrow(() -> new ResourceNotFoundException(
				"Package Not Found with package id : " + packageId));
		Page<PackageDiscription> findByPackages = this.discriptionRepository.findByPackages(pageable, packages);
		List<PackageDiscription> content = findByPackages.getContent();
		content.forEach(data->{
			System.err.println(data.toString());
		});
		PackageDiscriptionPagination discriptionPagination = new PackageDiscriptionPagination();
		List<PackageDiscriptionResponse> discriptionList = new ArrayList<PackageDiscriptionResponse>();
		content.forEach(data->{
			PackageDiscriptionResponse discriptionResponse =new PackageDiscriptionResponse();
			discriptionResponse.setDescription(data.getDescription());
			discriptionResponse.setId(data.getId());
			discriptionResponse.setPackageId(data.getPackages().getId());
			discriptionList.add(discriptionResponse);
		});
		discriptionPagination.setDiscriptionResponses(discriptionList);
		discriptionPagination.setLastPage(findByPackages.isLast());
		discriptionPagination.setPageNumber(findByPackages.getNumber());
		discriptionPagination.setPageSize(findByPackages.getSize());
		discriptionPagination.setTotalElement(findByPackages.getTotalElements());
		discriptionPagination.setTotalPages(findByPackages.getTotalPages());

		return discriptionPagination;
	}

	@Override
	public PackageDiscriptionResponse findByDescriptionId(long descriptionId) {
		PackageDiscription save = this.discriptionRepository.findById(descriptionId).orElseThrow(() -> new ResourceNotFoundException(
				"DescriptionId Not Found with package id : " + descriptionId));
		PackageDiscriptionResponse discriptionResponse = new PackageDiscriptionResponse();
		discriptionResponse.setId(save.getId());
		discriptionResponse.setDescription(save.getDescription());
		
		return discriptionResponse;
	}

	@Override
	public void deleteDescription(long descriptionId) {
		PackageDiscription save = this.discriptionRepository.findById(descriptionId).orElseThrow(() -> new ResourceNotFoundException(
				"DescriptionId Not Found with package id : " + descriptionId));
		System.err.println(save.toString());
		this.discriptionRepository.delete(save);

	}

}
