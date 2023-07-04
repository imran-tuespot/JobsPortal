package com.tuespotsolutions.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.AssignedPackages;
import com.tuespotsolutions.entity.PackageDiscription;
import com.tuespotsolutions.entity.Packages;
import com.tuespotsolutions.models.PackageDiscriptionResponse;
import com.tuespotsolutions.models.PackageWithActiveStatusListForCompany;
import com.tuespotsolutions.models.PackagesRequest;
import com.tuespotsolutions.models.PackagesResponse;
import com.tuespotsolutions.models.PackagesResponseWithPagination;
import com.tuespotsolutions.repository.AssignedPackagesRepository;
import com.tuespotsolutions.repository.PackagesRepository;
import com.tuespotsolutions.service.PackagesService;
import com.tuespotsolutions.util.ConstantConfiguration;

@Service
public class PackagesServiceImpl implements PackagesService {

	@Autowired
	PackagesRepository packagesRepository;

	@Autowired
	AssignedPackagesRepository assignedPackagesRepository;

	@Override
	public PackagesResponse savePackages(PackagesRequest packagesRequest) {

		java.util.Date utilDate = new java.util.Date();
		Timestamp ts = new Timestamp(utilDate.getTime());
		java.sql.Date sqlDate = new java.sql.Date(ts.getTime());

		Packages packages = new Packages();
		packages.setCandidate(packagesRequest.isCandidate());
		packages.setCompany(packagesRequest.isCompany());
		packages.setCreatedOn(sqlDate);
		packages.setCreatedBy(packagesRequest.getUserId());
		packages.setDays(packagesRequest.getDays());
		packages.setDiscount(packagesRequest.getDiscount());
		packages.setModifiedBy(packagesRequest.getUserId());
		packages.setModifiedOn(sqlDate);
		packages.setName(packagesRequest.getName());
		packages.setPrice(packagesRequest.getPrice());
		packages.setType(packagesRequest.getType());
		packages.setStatus(false);
		packages.setDefaultPackage(false);
		Packages save = this.packagesRepository.save(packages);
		PackagesResponse packagesResponse = new PackagesResponse();
		packagesResponse.setCandidate(save.isCandidate());
		packagesResponse.setCompany(save.isCompany());
		packagesResponse.setDays(save.getDays());
		packagesResponse.setDiscount(save.getDiscount());
		packagesResponse.setId(save.getId());
		packagesResponse.setName(save.getName());
		packagesResponse.setPrice(save.getPrice());
		packagesResponse.setType(save.getType());
		packagesResponse.setStatus(save.isStatus());
		packagesResponse.setDefaultPackage(save.isDefaultPackage());
		return packagesResponse;
	}

	@Override
	public PackagesResponse updatePackages(PackagesRequest packagesRequest) {

		System.out.println(packagesRequest.toString());

		java.util.Date utilDate = new java.util.Date();
		Timestamp ts = new Timestamp(utilDate.getTime());
		java.sql.Date sqlDate = new java.sql.Date(ts.getTime());

		Packages packages = this.packagesRepository.findById(packagesRequest.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Package Not Found"));
		packages.setCandidate(packagesRequest.isCandidate());
		packages.setCompany(packagesRequest.isCompany());
		packages.setCreatedBy(packagesRequest.getUserId());
		packages.setDays(packagesRequest.getDays());
		packages.setDiscount(packagesRequest.getDiscount());
		packages.setModifiedBy(packagesRequest.getUserId());
		packages.setModifiedOn(sqlDate);
		packages.setName(packagesRequest.getName());
		packages.setPrice(packagesRequest.getPrice());
		packages.setType(packagesRequest.getType());
		packages.setStatus(packagesRequest.isStatus());
		packages.setDefaultPackage(packagesRequest.isDefaultPackage());
		
		
		System.err.println(packages.toString());
		
		Packages save = this.packagesRepository.save(packages);

		PackagesResponse packagesResponse = new PackagesResponse();
		packagesResponse.setCandidate(save.isCandidate());
		packagesResponse.setCompany(save.isCompany());
		packagesResponse.setDays(save.getDays());
		packagesResponse.setDiscount(save.getDiscount());
		packagesResponse.setId(save.getId());
		packagesResponse.setName(save.getName());
		packagesResponse.setPrice(save.getPrice());
		packagesResponse.setType(save.getType());
		packagesRequest.setStatus(save.isStatus());
		packagesRequest.setDefaultPackage(save.isDefaultPackage());
		;
		return packagesResponse;
	}

	@Override
	public PackagesResponseWithPagination findAllPackages(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Packages> findAll = this.packagesRepository.findAll(pageable);
		List<Packages> content = findAll.getContent();
		List<PackagesResponse> packagesResponses = new ArrayList<PackagesResponse>();
		content.forEach(data -> {
			PackagesResponse packagesResponse = new PackagesResponse();
			packagesResponse.setCandidate(data.isCandidate());
			packagesResponse.setCompany(data.isCompany());
			packagesResponse.setDays(data.getDays());
			packagesResponse.setDiscount(data.getDiscount());
			packagesResponse.setId(data.getId());
			packagesResponse.setName(data.getName());
			packagesResponse.setPrice(data.getPrice());
			packagesResponse.setType(data.getType());
			packagesResponse.setStatus(data.isStatus());
			packagesResponse.setDefaultPackage(data.isDefaultPackage());
			List<PackageDiscription> packageDiscriptions = data.getPackageDiscriptions();
			List<PackageDiscriptionResponse> discriptionResponses = new ArrayList<PackageDiscriptionResponse>();
			packageDiscriptions.forEach(disc -> {
				PackageDiscriptionResponse discriptionResponse = new PackageDiscriptionResponse();
				discriptionResponse.setId(disc.getId());
				discriptionResponse.setDescription(disc.getDescription());
				discriptionResponse.setPackageId(disc.getPackages().getId());
				discriptionResponses.add(discriptionResponse);
			});
			packagesResponses.add(packagesResponse);
			packagesResponse.setDiscription(discriptionResponses);
		});

		PackagesResponseWithPagination packagesResponseWithPagination = new PackagesResponseWithPagination();
		packagesResponseWithPagination.setContent(packagesResponses);
		packagesResponseWithPagination.setLastPage(findAll.isLast());
		packagesResponseWithPagination.setPageNumber(findAll.getNumber());
		packagesResponseWithPagination.setPageSize(findAll.getSize());
		packagesResponseWithPagination.setTotalElement(findAll.getTotalElements());
		packagesResponseWithPagination.setTotalPages(findAll.getTotalPages());

		return packagesResponseWithPagination;
	}

	@Override
	public PackagesResponse findByPackagesId(Long packageId) {
		Packages packages = this.packagesRepository.findById(packageId).orElseThrow(
				() -> new ResourceNotFoundException("Package is not exist with package id : " + packageId));
		PackagesResponse packagesResponse = new PackagesResponse();
		packagesResponse.setCandidate(packages.isCandidate());
		packagesResponse.setCompany(packages.isCompany());
		packagesResponse.setDays(packages.getDays());
		packagesResponse.setDiscount(packages.getDiscount());
		packagesResponse.setId(packages.getId());
		packagesResponse.setName(packages.getName());
		packagesResponse.setPrice(packages.getPrice());
		packagesResponse.setType(packages.getType());
		packagesResponse.setStatus(packages.isStatus());
		packagesResponse.setDefaultPackage(packages.isDefaultPackage());
		return packagesResponse;
	}

	@Override
	public void deletePackage(Long packageId) {
		Packages packages = this.packagesRepository.findById(packageId).orElseThrow(
				() -> new ResourceNotFoundException("Package is not exist with package id : " + packageId));
		this.packagesRepository.delete(packages);

	}

	@Override
	public PackagesResponseWithPagination findPackagesByCompany(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Packages> findByCompany = this.packagesRepository.findByCompany(pageable, true);
		List<Packages> content = findByCompany.getContent();
		List<PackagesResponse> packagesResponses = new ArrayList<PackagesResponse>();
		content.forEach(data -> {
			if(data.isStatus() && !data.isDefaultPackage()) {
			PackagesResponse packagesResponse = new PackagesResponse();
			packagesResponse.setCandidate(data.isCandidate());
			packagesResponse.setCompany(data.isCompany());
			packagesResponse.setDays(data.getDays());
			packagesResponse.setId(data.getId());
			packagesResponse.setName(data.getName());
			packagesResponse.setPrice(data.getPrice());
			packagesResponse.setType(data.getType());
			packagesResponse.setStatus(data.isStatus());
			packagesResponse.setDefaultPackage(data.isDefaultPackage());
			List<PackageDiscription> packageDiscriptions = data.getPackageDiscriptions();
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

			packagesResponses.add(packagesResponse);
			}
		});
		PackagesResponseWithPagination packagesResponseWithPagination = new PackagesResponseWithPagination();
		packagesResponseWithPagination.setContent(packagesResponses);
		packagesResponseWithPagination.setLastPage(findByCompany.isLast());
		packagesResponseWithPagination.setPageNumber(findByCompany.getNumber());
		packagesResponseWithPagination.setPageSize(findByCompany.getSize());
		packagesResponseWithPagination.setTotalElement(findByCompany.getTotalElements());
		packagesResponseWithPagination.setTotalPages(findByCompany.getTotalPages());
		return packagesResponseWithPagination;
	}

	@Override
	public PackagesResponseWithPagination findPackagesByCandidate(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Packages> findByCompany = this.packagesRepository.findByCandidate(pageable, true);
		List<Packages> content = findByCompany.getContent();
		List<PackagesResponse> packagesResponses = new ArrayList<PackagesResponse>();
		content.forEach(data -> {
			if(data.isStatus() && !data.isDefaultPackage()) {
			PackagesResponse packagesResponse = new PackagesResponse();
			packagesResponse.setCandidate(data.isCandidate());
			packagesResponse.setCompany(data.isCompany());
			packagesResponse.setDays(data.getDays());
			packagesResponse.setDiscount(data.getDiscount());
			packagesResponse.setId(data.getId());
			packagesResponse.setName(data.getName());
			packagesResponse.setPrice(data.getPrice());
			packagesResponse.setType(data.getType());
			packagesResponse.setStatus(data.isStatus());
			packagesResponse.setDefaultPackage(data.isDefaultPackage());
			List<PackageDiscription> packageDiscriptions = data.getPackageDiscriptions();
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
			packagesResponses.add(packagesResponse);
			}
		});
		PackagesResponseWithPagination packagesResponseWithPagination = new PackagesResponseWithPagination();
		packagesResponseWithPagination.setContent(packagesResponses);
		packagesResponseWithPagination.setLastPage(findByCompany.isLast());
		packagesResponseWithPagination.setPageNumber(findByCompany.getNumber());
		packagesResponseWithPagination.setPageSize(findByCompany.getSize());
		packagesResponseWithPagination.setTotalElement(findByCompany.getTotalElements());
		packagesResponseWithPagination.setTotalPages(findByCompany.getTotalPages());
		return packagesResponseWithPagination;

	}

	@Override
	public List<PackagesResponse> findCompanyPackagesForWesite() {

		List<Packages> findByCompany = this.packagesRepository.findByCompany(true);

		System.err.println(findByCompany);

		List<PackagesResponse> packagesResponses = new ArrayList<PackagesResponse>();

		findByCompany.forEach(data -> {
			if(data.isStatus()) {
			PackagesResponse packagesResponse = new PackagesResponse();
			packagesResponse.setCandidate(data.isCandidate());
			packagesResponse.setCompany(data.isCompany());
			packagesResponse.setDays(data.getDays());
			packagesResponse.setDiscount(data.getDiscount());
			packagesResponse.setId(data.getId());
			packagesResponse.setName(data.getName());
			packagesResponse.setPrice(data.getPrice());
			packagesResponse.setType(data.getType());
			packagesResponse.setStatus(data.isStatus());
			packagesResponse.setDefaultPackage(data.isDefaultPackage());
			List<PackageDiscription> packageDiscriptions = data.getPackageDiscriptions();
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
			packagesResponses.add(packagesResponse);
			}
		});
		return packagesResponses;
	}

	@Override
	public List<PackagesResponse> findCandidatePackagesForWesite() {
		List<Packages> findByCandidate = this.packagesRepository.findByCandidate(true);
		List<PackagesResponse> packagesResponses = new ArrayList<PackagesResponse>();
		findByCandidate.forEach(data -> {
			if(data.isStatus()) {
			PackagesResponse packagesResponse = new PackagesResponse();
			packagesResponse.setCandidate(data.isCandidate());
			packagesResponse.setCompany(data.isCompany());
			packagesResponse.setDays(data.getDays());
			packagesResponse.setDiscount(data.getDiscount());
			packagesResponse.setId(data.getId());
			packagesResponse.setName(data.getName());
			packagesResponse.setPrice(data.getPrice());
			packagesResponse.setType(data.getType());
			packagesResponse.setStatus(data.isStatus());
			packagesResponse.setDefaultPackage(data.isDefaultPackage());
			List<PackageDiscription> packageDiscriptions = data.getPackageDiscriptions();
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
			packagesResponses.add(packagesResponse);
			}
		});
		return packagesResponses;
	}

}
