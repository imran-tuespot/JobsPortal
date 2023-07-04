package com.tuespotsolutions.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.AssignedPackages;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.City;
import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.District;
import com.tuespotsolutions.entity.Packages;
import com.tuespotsolutions.entity.State;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.models.AssignedPackageResponse;
import com.tuespotsolutions.models.AssignedPackagesRequest;
import com.tuespotsolutions.models.CandidateAssignedPackageList;
import com.tuespotsolutions.models.CompanyAssignedPackageList;
import com.tuespotsolutions.models.PackageWithActiveStatusListForCompany;
import com.tuespotsolutions.models.TransactionResponse;
import com.tuespotsolutions.repository.AssignedPackagesRepository;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.CityRepository;
import com.tuespotsolutions.repository.CompanyRepository;
import com.tuespotsolutions.repository.DistrictRepository;
import com.tuespotsolutions.repository.PackagesRepository;
import com.tuespotsolutions.repository.StateRepository;
import com.tuespotsolutions.repository.UserRepository;
import com.tuespotsolutions.service.AssignedPackagesService;
import com.tuespotsolutions.util.ConstantConfiguration;

@Service
public class AssignedPackagesServiceImpl implements AssignedPackagesService {

	@Autowired
	private AssignedPackagesRepository assignedPackagesRepository;

	@Autowired
	private PackagesRepository packagesRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private StateRepository stateRepository;

	@Override
	public AssignedPackageResponse assignedPackage(AssignedPackagesRequest assignedPackagesRequest) {

		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);
		String format = timeStamp.format(utilDate.getTime());

		Packages packages = this.packagesRepository.findById(assignedPackagesRequest.getPackageId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Package is not exist with packageId : " + assignedPackagesRequest.getPackageId()));
		User user = this.userRepository.findById(assignedPackagesRequest.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"User is not exist with userId : " + assignedPackagesRequest.getUserId()));

		AssignedPackages findByUserId = this.assignedPackagesRepository.findByUserId(user.getId());

		if (findByUserId != null) {
			findByUserId.setAssignDate(format);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); // Using today's date
			c.add(Calendar.DATE, packages.getDays());
			String endDate = timeStamp.format(c.getTime());
			findByUserId.setEndDate(endDate);
			findByUserId.setAssignedDays(packages.getDays());
			findByUserId.setPendingDays(packages.getDays());
			findByUserId.setPackageId(packages.getId());
			findByUserId.setStatus(true);
			findByUserId.setUserId(user.getId());
			findByUserId.setUserType(user.getUserType());
			AssignedPackages save = this.assignedPackagesRepository.save(findByUserId);

			AssignedPackageResponse assignedPackageResponse = new AssignedPackageResponse();
			assignedPackageResponse.setId(save.getId());
			assignedPackageResponse.setAssignDate(save.getAssignDate());
			assignedPackageResponse.setEndDate(save.getEndDate());
			assignedPackageResponse.setPendingDays(save.getPendingDays());
			assignedPackageResponse.setDays(save.getAssignedDays());
			assignedPackageResponse.setStatus(save.isStatus());
			return assignedPackageResponse;
		} else {

			AssignedPackages assignedPackages = new AssignedPackages();
			assignedPackages.setAssignDate(format);

			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); // Using today's date
			c.add(Calendar.DATE, packages.getDays());
			String endDate = timeStamp.format(c.getTime());

			assignedPackages.setEndDate(endDate);
			assignedPackages.setAssignedDays(packages.getDays());
			assignedPackages.setPendingDays(packages.getDays());
			assignedPackages.setPackageId(packages.getId());
			assignedPackages.setStatus(true);
			assignedPackages.setUserId(user.getId());
			assignedPackages.setUserType(user.getUserType());
			AssignedPackages save = this.assignedPackagesRepository.save(assignedPackages);

			AssignedPackageResponse assignedPackageResponse = new AssignedPackageResponse();
			assignedPackageResponse.setId(save.getId());
			assignedPackageResponse.setAssignDate(save.getAssignDate());
			assignedPackageResponse.setEndDate(save.getEndDate());
			assignedPackageResponse.setPendingDays(save.getPendingDays());
			assignedPackageResponse.setDays(save.getAssignedDays());
			assignedPackageResponse.setStatus(save.isStatus());
			return assignedPackageResponse;
		}

	}

	@Override
	public List<PackageWithActiveStatusListForCompany> getCompanyPackageList(Long userId) {
		List<AssignedPackages> findByUserIdAndUserType = this.assignedPackagesRepository.findByUserIdAndUserType(userId,
				ConstantConfiguration.COMPANY);
		List<PackageWithActiveStatusListForCompany> packageWithActiveStatusListForCompanies = new ArrayList<PackageWithActiveStatusListForCompany>();

		findByUserIdAndUserType.forEach(data -> {

			AssignedPackages save = new AssignedPackages();
			try {
				java.util.Date utilDate = new java.util.Date();
				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				String format = timeStamp.format(utilDate.getTime());

				Date endDate = timeStamp.parse(data.getEndDate());
				Date currentDate = timeStamp.parse(format);
				long timeDiff = Math.abs(endDate.getTime() - currentDate.getTime());
				long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
				data.setPendingDays((int) daysDiff);

				save = this.assignedPackagesRepository.save(data);

				Packages packages = this.packagesRepository.findById(save.getPackageId())
						.orElseThrow(() -> new ResourceNotFoundException("Package not found "));

				if (save.getPendingDays() <= 0 && packages.getType().equalsIgnoreCase("Free")) {
					this.assignedPackagesRepository.delete(data);
				}

				if (save.getPendingDays() <= 0 && packages.getType().equalsIgnoreCase("Paid")) {
					save.setStatus(false);
					save = this.assignedPackagesRepository.save(save);
				}

				if (save.getPendingDays() > 0 && packages.getType().equalsIgnoreCase("Paid")) {
					save.setStatus(true);
					save = this.assignedPackagesRepository.save(save);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

			PackageWithActiveStatusListForCompany activeStatusListForCompany = new PackageWithActiveStatusListForCompany();
			activeStatusListForCompany.setDays(save.getAssignedDays());
			activeStatusListForCompany.setPackageId(save.getPackageId());
			activeStatusListForCompany.setPendingDays(save.getPendingDays());
			activeStatusListForCompany.setStartDate(save.getAssignDate());
			activeStatusListForCompany.setEndDate(save.getEndDate());
			activeStatusListForCompany.setStatus(data.isStatus());
			Packages packages = this.packagesRepository.findById(save.getPackageId())
					.orElseThrow(() -> new ResourceNotFoundException("Package not found "));
			activeStatusListForCompany.setDiscount(packages.getDiscount());
			activeStatusListForCompany.setId(save.getId());
			activeStatusListForCompany.setName(packages.getName());
			activeStatusListForCompany.setPrice(packages.getPrice());
			activeStatusListForCompany.setType(packages.getType());
			Integer pendingDaysPers = (save.getPendingDays() * 100) / save.getAssignedDays();
			activeStatusListForCompany.setPendingDaysPercentage(pendingDaysPers);
			packageWithActiveStatusListForCompanies.add(activeStatusListForCompany);
		});
		return packageWithActiveStatusListForCompanies;
	}

	@Override
	public List<PackageWithActiveStatusListForCompany> getCandidatePackageList(Long userId) {
		List<AssignedPackages> findByUserIdAndUserType = this.assignedPackagesRepository.findByUserIdAndUserType(userId,
				ConstantConfiguration.CANDIDATE);
		List<PackageWithActiveStatusListForCompany> packageWithActiveStatusListForCompanies = new ArrayList<PackageWithActiveStatusListForCompany>();

		findByUserIdAndUserType.forEach(data -> {

			AssignedPackages save = new AssignedPackages();
			try {
				java.util.Date utilDate = new java.util.Date();
				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				String format = timeStamp.format(utilDate.getTime());

				Date endDate = timeStamp.parse(data.getEndDate());
				Date currentDate = timeStamp.parse(format);
				long timeDiff = Math.abs(endDate.getTime() - currentDate.getTime());
				long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
				data.setPendingDays((int) daysDiff);

				save = this.assignedPackagesRepository.save(data);

				Packages packages = this.packagesRepository.findById(save.getPackageId())
						.orElseThrow(() -> new ResourceNotFoundException("Package not found "));

				if (save.getPendingDays() <= 0 && packages.getType().equalsIgnoreCase("Free")) {
					this.assignedPackagesRepository.delete(data);
				}

				if (save.getPendingDays() <= 0 && packages.getType().equalsIgnoreCase("Paid")) {
					save.setStatus(false);
					save = this.assignedPackagesRepository.save(save);
				}

				if (save.getPendingDays() > 0 && packages.getType().equalsIgnoreCase("Paid")) {
					save.setStatus(true);
					save = this.assignedPackagesRepository.save(save);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

			PackageWithActiveStatusListForCompany activeStatusListForCompany = new PackageWithActiveStatusListForCompany();
			activeStatusListForCompany.setDays(save.getAssignedDays());
			activeStatusListForCompany.setPackageId(save.getPackageId());
			activeStatusListForCompany.setPendingDays(save.getPendingDays());
			activeStatusListForCompany.setStartDate(save.getAssignDate());
			activeStatusListForCompany.setEndDate(save.getEndDate());
			activeStatusListForCompany.setStatus(data.isStatus());
			Packages packages = this.packagesRepository.findById(save.getPackageId())
					.orElseThrow(() -> new ResourceNotFoundException("Package not found "));
			activeStatusListForCompany.setDiscount(packages.getDiscount());
			activeStatusListForCompany.setId(save.getId());
			activeStatusListForCompany.setName(packages.getName());
			activeStatusListForCompany.setPrice(packages.getPrice());
			activeStatusListForCompany.setType(packages.getType());
			Integer pendingDaysPers = (save.getPendingDays() * 100) / save.getAssignedDays();
			activeStatusListForCompany.setPendingDaysPercentage(pendingDaysPers);
			packageWithActiveStatusListForCompanies.add(activeStatusListForCompany);
		});
		return packageWithActiveStatusListForCompanies;
	}

	@Override
	public AssignedPackageResponse updateAssignedPackage(Long assignedPackageId) {
		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);
		String format = timeStamp.format(utilDate.getTime());

		AssignedPackages save = this.assignedPackagesRepository.findById(assignedPackageId)
				.orElseThrow(() -> new ResourceNotFoundException("Assigned package not found "));

		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		c.add(Calendar.DATE, save.getAssignedDays());
		String endDate = timeStamp.format(c.getTime());

		save.setAssignDate(format);
		save.setEndDate(endDate);
		save.setPendingDays(save.getAssignedDays());

		this.assignedPackagesRepository.save(save);

		AssignedPackageResponse assignedPackageResponse = new AssignedPackageResponse();
		assignedPackageResponse.setId(save.getId());
		assignedPackageResponse.setAssignDate(save.getAssignDate());
		assignedPackageResponse.setEndDate(save.getEndDate());
		assignedPackageResponse.setPendingDays(save.getPendingDays());
		assignedPackageResponse.setDays(save.getAssignedDays());
		assignedPackageResponse.setStatus(save.isStatus());
		return assignedPackageResponse;
	}

	@Override
	public List<CompanyAssignedPackageList> getCompmanyAssignedPackageList() {
		List<AssignedPackages> companyTransactionList = this.assignedPackagesRepository
				.findByUserType(ConstantConfiguration.COMPANY);

		List<CompanyAssignedPackageList> assignedPackageLists = new ArrayList<CompanyAssignedPackageList>();
		companyTransactionList.forEach(data -> {

			User user = this.userRepository.findById(data.getId())
					.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

			Company company = this.companyRepository.findById(user.getTypeId())
					.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

			Packages packages = this.packagesRepository.findById(data.getPackageId())
					.orElseThrow(() -> new ResourceNotFoundException("Package Not Found"));

			String companyLocation = "";
			
			

			if (company.getCity() != null && company.getCity() != 0) {
				City city = this.cityRepository.findById(company.getCity())
						.orElseThrow(() -> new ResourceNotFoundException("City Not Found"));

				companyLocation += city.getName();

			}

			if (company.getDistrict() != null && company.getDistrict() != 0) {
				District district = this.districtRepository.findById(company.getDistrict())
						.orElseThrow(() -> new ResourceNotFoundException("District Not Found"));

				companyLocation += district.getDistrictTitle();
			}

			if (company.getState() != null && company.getDistrict() != 0) {
				State state = this.stateRepository.findById(company.getState())
						.orElseThrow(() -> new ResourceNotFoundException("State Not Found"));
				companyLocation += state.getStateTitle();
			}

			CompanyAssignedPackageList assignedPackageList = new CompanyAssignedPackageList();
			assignedPackageList.setId(data.getPackageId());
			assignedPackageList.setCompanyName(company.getName());
			assignedPackageList
					.setCompanyLocation(company.getAddress() + "," + companyLocation + "," + company.getPinCode());
			assignedPackageList.setCompanyEmail(company.getEmail());
			assignedPackageList.setMobileNumber(company.getMobileNumber());
			assignedPackageList.setPackageAssignedDays(data.getAssignedDays() + "");
			assignedPackageList.setPackagePendingDays(data.getPendingDays() + "");
			assignedPackageList.setPackageName(packages.getName());
			assignedPackageList.setPackagePrice(packages.getPrice() + "");
			assignedPackageList.setStartDate(data.getAssignDate());
			assignedPackageList.setEndDate(data.getEndDate());
			assignedPackageList.setStatus(data.isStatus());
			assignedPackageLists.add(assignedPackageList);
		});
		return assignedPackageLists;
	}

	@Override
	public List<CandidateAssignedPackageList> getCandidateAssignedPackageList() {
		List<AssignedPackages> candidateTransactionList = this.assignedPackagesRepository
				.findByUserType(ConstantConfiguration.CANDIDATE);

		List<AssignedPackages> companyTransactionList = this.assignedPackagesRepository
				.findByUserType(ConstantConfiguration.COMPANY);

		List<CandidateAssignedPackageList> assignedPackageLists = new ArrayList<CandidateAssignedPackageList>();
		candidateTransactionList.forEach(data -> {

			User user = this.userRepository.findById(data.getId())
					.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

			Candidate candidate = this.candidateRepository.findById(user.getTypeId())
					.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

			Packages packages = this.packagesRepository.findById(data.getPackageId())
					.orElseThrow(() -> new ResourceNotFoundException("Package Not Found"));

			String companyLocation = "";

			if (candidate.getCity() != null && candidate.getCity() > 0) {
				City city = this.cityRepository.findById(candidate.getCity())
						.orElseThrow(() -> new ResourceNotFoundException("District Not Found"));
				companyLocation += city.getName();
			}
			
			if (candidate.getDistrict() != null && candidate.getDistrict() > 0) {
				District district = this.districtRepository.findById(candidate.getDistrict())
						.orElseThrow(() -> new ResourceNotFoundException("District Not Found"));	
				companyLocation += district.getDistrictTitle();
			}
			
			if (candidate.getState() != null && candidate.getState() > 0) {
				State state = this.stateRepository.findById(candidate.getState())
						.orElseThrow(() -> new ResourceNotFoundException("State Not Found"));
				companyLocation += state.getStateTitle();
			}

			CandidateAssignedPackageList assignedPackageList = new CandidateAssignedPackageList();
			assignedPackageList.setId(data.getPackageId());
			assignedPackageList.setCandidateName(candidate.getName());
			assignedPackageList.setCandidateLocation(candidate.getAddress()+","+companyLocation+","+candidate.getPinCode());
			assignedPackageList.setCanidateEmail(candidate.getEmail());
			assignedPackageList.setMobileNumber(candidate.getMobileNumber());
			assignedPackageList.setPackageAssignedDays(data.getAssignedDays() + "");
			assignedPackageList.setPackagePendingDays(data.getPendingDays() + "");
			assignedPackageList.setPackageName(packages.getName());
			assignedPackageList.setPackagePrice(packages.getPrice() + "");
			assignedPackageList.setStartDate(data.getAssignDate());
			assignedPackageList.setEndDate(data.getEndDate());
			assignedPackageList.setStatus(data.isStatus());
			assignedPackageLists.add(assignedPackageList);
		});
		return assignedPackageLists;
	}

}
