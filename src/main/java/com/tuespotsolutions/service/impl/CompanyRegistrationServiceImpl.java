package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.FieldAlreadyExistException;
import com.tuespotsolutions.customexception.OtpNotSendedException;
import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.email.service.EmailServiceImpl;
import com.tuespotsolutions.entity.City;
import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.District;
import com.tuespotsolutions.entity.RegistrationOtp;
import com.tuespotsolutions.entity.Roles;
import com.tuespotsolutions.entity.State;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.models.CompanList;
import com.tuespotsolutions.models.CompanyDetailResponse;
import com.tuespotsolutions.models.CompanyListWithPagination;
import com.tuespotsolutions.models.CompanyProfileRequest;
import com.tuespotsolutions.models.CompanyProfileResponse;
import com.tuespotsolutions.models.CompanyRequest;
import com.tuespotsolutions.models.UpdateCompanyInterViewLink;
import com.tuespotsolutions.repository.AssignedPackagesRepository;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.CityRepository;
import com.tuespotsolutions.repository.CompanyRepository;
import com.tuespotsolutions.repository.DistrictRepository;
import com.tuespotsolutions.repository.PackagesRepository;
import com.tuespotsolutions.repository.RegistrationOtpRepository;
import com.tuespotsolutions.repository.RolesRepository;
import com.tuespotsolutions.repository.StateRepository;
import com.tuespotsolutions.repository.UserRepository;
import com.tuespotsolutions.service.CompanyRegistrationService;
import com.tuespotsolutions.util.ConstantConfiguration;
import com.tuespotsolutions.util.FileUpload;
import com.tuespotsolutions.util.OtpSender;

@Service
public class CompanyRegistrationServiceImpl implements CompanyRegistrationService {

	Logger logger = LoggerFactory.getLogger(CompanyRegistrationServiceImpl.class);

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private RolesRepository rolesRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RegistrationOtpRepository registrationOtpRepository;

	@Autowired
	EmailServiceImpl emailServiceImpl;
	
	

	@Value("${file.upload.path}")
	private String fileUploadUrl;
	
	@Value("${file.get.url}")
	private String fileGetUrl;

	@Override
	public Map<String, String> registerCompany(CompanyRequest companyRequest) {

		@SuppressWarnings("unchecked")
		Map<String, String> map = new HashedMap();
		if (companyRepository.existsByEmail(companyRequest.getEmail())) {
			Optional<Company> findByEmail = companyRepository.findByEmail(companyRequest.getEmail());
			if (findByEmail.isPresent()) {
				if (findByEmail.get().isStatus()) {
					logger.error("line no 92 FieldAlreadyExistException, message : Email is already exis "
							+ companyRequest.getEmail());
					throw new FieldAlreadyExistException("Email is already exist : " + companyRequest.getEmail());
				} else {
					Optional<User> findByUserTypeAndTypeId = userRepository
							.findByUserTypeAndTypeId(ConstantConfiguration.COMPANY, findByEmail.get().getId());
					userRepository.delete(findByUserTypeAndTypeId.get());
					Optional<RegistrationOtp> findByUserIdAndUserType = registrationOtpRepository
							.findByUserIdAndUserType(findByEmail.get().getId(), ConstantConfiguration.COMPANY);
					registrationOtpRepository.delete(findByUserIdAndUserType.get());
					companyRepository.delete(findByEmail.get());
				}
			}
		}

		if (companyRepository.existsByMobileNumber(companyRequest.getMobileNumber())) {
			Optional<Company> findByMobileNumber = companyRepository
					.findByMobileNumber(companyRequest.getMobileNumber());
			if (findByMobileNumber.isPresent()) {
				if (findByMobileNumber.get().isStatus()) {
					logger.error("line no 103 FieldAlreadyExistException, message : Mobile Number Already exis "
							+ companyRequest.getMobileNumber());
					throw new FieldAlreadyExistException(
							"Mobile Number Already exist : " + companyRequest.getMobileNumber());
				} else {

					Optional<User> findByUserTypeAndTypeId = userRepository
							.findByUserTypeAndTypeId(ConstantConfiguration.COMPANY, findByMobileNumber.get().getId());
					userRepository.delete(findByUserTypeAndTypeId.get());
					Optional<RegistrationOtp> findByUserIdAndUserType = registrationOtpRepository
							.findByUserIdAndUserType(findByMobileNumber.get().getId(), ConstantConfiguration.COMPANY);
					registrationOtpRepository.delete(findByUserIdAndUserType.get());
					companyRepository.delete(findByMobileNumber.get());
				}
			}
		}

		if (companyRepository.existsByName(companyRequest.getName())) {
			Optional<Company> findByName = companyRepository.findByName(companyRequest.getName());
			if (findByName.isPresent()) {
				if (findByName.get().isStatus()) {
					throw new FieldAlreadyExistException("Company Name Already exist : " + companyRequest.getName());
				} else {
					Optional<User> findByUserTypeAndTypeId = userRepository
							.findByUserTypeAndTypeId(ConstantConfiguration.COMPANY, findByName.get().getId());
					userRepository.delete(findByUserTypeAndTypeId.get());
					Optional<RegistrationOtp> findByUserIdAndUserType = registrationOtpRepository
							.findByUserIdAndUserType(findByName.get().getId(), ConstantConfiguration.COMPANY);
					registrationOtpRepository.delete(findByUserIdAndUserType.get());
					companyRepository.delete(findByName.get());

				}
			}
		}

		if (candidateRepository.existsByUsername(companyRequest.getUserName())) {
			throw new FieldAlreadyExistException("Username Already exist : " + companyRequest.getUserName());
		}

		if (companyRepository.existsByUsername(companyRequest.getUserName())) {
			Optional<Company> findByUsername = companyRepository.findByUsername(companyRequest.getUserName());
			if (findByUsername.isPresent()) {
				if (findByUsername.get().isStatus()) {
					throw new FieldAlreadyExistException("Username Already exist : " + companyRequest.getUserName());
				} else {
					Optional<User> findByUserTypeAndTypeId = userRepository
							.findByUserTypeAndTypeId(ConstantConfiguration.COMPANY, findByUsername.get().getId());
					userRepository.delete(findByUserTypeAndTypeId.get());
					Optional<RegistrationOtp> findByUserIdAndUserType = registrationOtpRepository
							.findByUserIdAndUserType(findByUsername.get().getId(), ConstantConfiguration.COMPANY);
					registrationOtpRepository.delete(findByUserIdAndUserType.get());
					companyRepository.delete(findByUsername.get());
				}
			}
		}

		java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

		int otp = new Random().nextInt(900000) + 100000;
		String sendOtpSms = OtpSender.sendOtpSms(companyRequest.getMobileNumber(), "Job Solutions", otp + "");
		if (!sendOtpSms.isEmpty()) {

			Optional<Company> exstCompanyUser = companyRepository.findByEmail(companyRequest.getEmail());
			if (exstCompanyUser.isPresent() && !exstCompanyUser.get().isStatus()) {

				if (sendOtpSms != null) {
					Optional<RegistrationOtp> registrationOtp = this.registrationOtpRepository
							.findByUserIdAndUserType(exstCompanyUser.get().getId(), ConstantConfiguration.COMPANY);

					if (registrationOtp.isPresent()) {
						registrationOtp.get().setOtp(otp + "");
						registrationOtp.get().setOtpRefrenceId(sendOtpSms);
						registrationOtp.get().setCreatedOn(sqlDate);
						registrationOtp.get().setModifiedOn(sqlDate);
						RegistrationOtp otpResp = registrationOtpRepository.save(registrationOtp.get());
						map.put("message", "You have recieved your otp on this number : "
								+ exstCompanyUser.get().getMobileNumber());
						map.put("otpId", otpResp.getId() + "");

					}
				}

			} else {

				if (sendOtpSms != null) {

					Company company = new Company();
					company.setName(companyRequest.getName());
					company.setUsername(companyRequest.getUserName());
					company.setEmail(companyRequest.getEmail());
					company.setMobileNumber(companyRequest.getMobileNumber());
					company.setAddress(companyRequest.getAddress());

					if (companyRequest.getStateId() > 0 && companyRequest.getStateId() + "" != ""
							&& companyRequest.getStateId() != null) {
						State state = this.stateRepository.findById(companyRequest.getStateId())
								.orElseThrow(() -> new ResourceNotFoundException(
										"State Id " + companyRequest.getStateId() + " is not exist"));
						company.setState(state.getStateId());
					} else {
						company.setState(0);
					}

					if (companyRequest.getDistrictId() > 0 && companyRequest.getDistrictId() + "" != ""
							&& companyRequest.getDistrictId() != null) {
						District district = this.districtRepository.findById(companyRequest.getDistrictId())
								.orElseThrow(() -> new ResourceNotFoundException(
										"District Id " + companyRequest.getDistrictId() + " is not exist"));
						company.setDistrict(district.getDistrictId());
					} else {
						company.setDistrict(0);
					}

					if (companyRequest.getCityId() > 0 && companyRequest.getCityId() + "" != ""
							&& companyRequest.getCityId() != null) {
						City city = this.cityRepository.findById(companyRequest.getCityId())
								.orElseThrow(() -> new ResourceNotFoundException(
										"City Id " + companyRequest.getCityId() + " is not exist"));
						company.setCity(city.getId());
					} else {
						company.setCity(0);
					}

					company.setPinCode(companyRequest.getPinCode());
					company.setBussinessLocation(companyRequest.getBusinessLocation());

					company.setPanCard(companyRequest.getPanCardNumber());
					company.setGstNo(companyRequest.getGstNumber());

					company.setCreatedOn(sqlDate);
					company.setModifiedOn(sqlDate);

					company.setStatus(false);
					System.out.println("line no 144 " + companyRequest);
					System.out.println("line no 145 " + companyRequest.getLogo());
					if (companyRequest.getLogo() != null && companyRequest.getLogo() != ""
							&& companyRequest.getLogo().contains("data:image")) {
						String uploadFile = FileUpload.uploadFile(companyRequest.getLogo(), fileUploadUrl);
						company.setLogo(uploadFile);
					}

					company.setStatus(false);
					Company save = this.companyRepository.save(company);

					if (save != null) {
						// save user
						User user = new User();
						user.setApplicantName(save.getName());
						user.setUsername(save.getUsername());
						user.setEmail(save.getEmail());
						user.setMobileNumber(save.getMobileNumber());
						Roles role = this.rolesRepository.findById(1L)
								.orElseThrow(() -> new ResourceNotFoundException("Role Id " + 1 + " is not exist"));
						Set<Roles> roles = new HashSet<Roles>();
						roles.add(role);
						user.setRoles(roles);
						String encodedPassword = passwordEncoder.encode(companyRequest.getPassword());
						user.setPassword(encodedPassword);
						user.setCreatedOn(sqlDate);
						user.setModifiedOn(sqlDate);
						user.setUserType(ConstantConfiguration.COMPANY);
						user.setTypeId(save.getId());
						user.setStatus(false);
						this.userRepository.save(user);
						RegistrationOtp registrationOtp = new RegistrationOtp();
						registrationOtp.setOtp(otp + "");
						registrationOtp.setOtpRefrenceId(sendOtpSms);
						registrationOtp.setUserId(save.getId());
						registrationOtp.setUserType(ConstantConfiguration.COMPANY);
						registrationOtp.setCreatedOn(sqlDate);
						registrationOtp.setModifiedOn(sqlDate);
						RegistrationOtp otpResp = registrationOtpRepository.save(registrationOtp);

						map.put("message", "You have recieved your otp on this number : " + save.getMobileNumber());
						map.put("otpId", otpResp.getId() + "");
					}
				}

			}
		} else {
			throw new OtpNotSendedException(
					"Kindly Check your mobile number we are not able to send otp on this numner : "
							+ companyRequest.getMobileNumber());
		}

		return map;
	}

	@Override
	public CompanyProfileResponse getCompanyProfileDetails(long companyId) {
		Company company = this.companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Profile Detail not exist with id : " + companyId));
		CompanyProfileResponse companyProfileResponse = new CompanyProfileResponse();
		companyProfileResponse.setId(company.getId());
		companyProfileResponse.setAddress(company.getAddress());
		companyProfileResponse.setBussinessLocation(company.getBussinessLocation());
		companyProfileResponse.setEmail(company.getEmail());
		companyProfileResponse.setGstNo(company.getGstNo());
		if (company.getLogo() != null) {
			companyProfileResponse.setLogo(fileGetUrl + company.getLogo());
		}
		companyProfileResponse.setMobileNumber(company.getMobileNumber());
		companyProfileResponse.setName(company.getName());
		companyProfileResponse.setPanCard(company.getPanCard());
		companyProfileResponse.setPinCode(company.getPinCode());
		companyProfileResponse.setCity(company.getCity());
		companyProfileResponse.setDistrict(company.getDistrict());
		companyProfileResponse.setState(company.getState());
		return companyProfileResponse;
	}

	@Override
	public Map<String, String> updateCompanyProfile(CompanyProfileRequest companyProfileResponse) {

		Company companyData = this.companyRepository.findById(companyProfileResponse.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Profile Detail not exist with id : " +companyProfileResponse.getId()));
		companyData.setName(companyProfileResponse.getName());
		companyData.setEmail(companyProfileResponse.getEmail());
		companyData.setMobileNumber(companyProfileResponse.getMobileNumber());
		companyData.setAddress(companyProfileResponse.getAddress());

		if (companyProfileResponse.getState()  > 0 && companyProfileResponse.getState() + "" != ""
				&& companyProfileResponse.getState() != null) {
			State state = this.stateRepository.findById(companyProfileResponse.getState())
					.orElseThrow(() -> new ResourceNotFoundException(
							"State Id " + companyProfileResponse.getState() + " is not exist"));
			companyData.setState(state.getStateId());
		} else {
			companyData.setState(0);
		}

		if (companyProfileResponse.getDistrict() > 0 && companyProfileResponse.getDistrict() + "" != ""
				&& companyProfileResponse.getDistrict() != null) {
			District district = this.districtRepository.findById(companyProfileResponse.getDistrict())
					.orElseThrow(() -> new ResourceNotFoundException(
							"District Id " + companyProfileResponse.getDistrict() + " is not exist"));
			companyData.setDistrict(district.getDistrictId());
		} else {
			companyData.setDistrict(0);
		}

		if (companyProfileResponse.getCity() > 0 && companyProfileResponse.getCity() + "" != ""
				&& companyProfileResponse.getCity() != null) {
			City city = this.cityRepository.findById(companyProfileResponse.getCity())
					.orElseThrow(() -> new ResourceNotFoundException(
							"City Id " + companyProfileResponse.getCity() + " is not exist"));
			companyData.setCity(city.getId());
		} else {
			companyData.setCity(0);
		}

		companyData.setPinCode(companyProfileResponse.getPinCode());
		companyData.setBussinessLocation(companyProfileResponse.getBussinessLocation());

		companyData.setPanCard(companyProfileResponse.getPanCard());
		companyData.setGstNo(companyProfileResponse.getGstNo());
		
		java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		companyData.setModifiedOn(sqlDate);

		
		if (companyProfileResponse.getLogo() != null && companyProfileResponse.getLogo() != ""
				&& companyProfileResponse.getLogo().contains("data:image")) {
			String uploadFile = FileUpload.uploadFile(companyProfileResponse.getLogo(), fileUploadUrl);
			companyData.setLogo(uploadFile);
		}

		companyData.setStatus(false);
		Company save = this.companyRepository.save(companyData);
		Map<String, String>  response = new HashMap<String, String>();
		if(save  != null) {
			response.put("status", "Profile Updated !!");
			return response;
		}else {
			response.put("status", "Internal Server Error");
			return response;
		}
		
		
	}

	@Override
	public CompanyListWithPagination getCompanyList(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "id"));
		
		Page<Company> findAll = this.companyRepository.findAll(pageable);
		List<Company> content = findAll.getContent();
		CompanyListWithPagination companyListWithPagination = new CompanyListWithPagination();
		List<CompanList> companyList = new ArrayList<CompanList>();
		content.forEach(company->{
			CompanList companyProfileResponse = new CompanList();
			companyProfileResponse.setId(company.getId());
			companyProfileResponse.setAddress(company.getAddress());
			companyProfileResponse.setBussinessLocation(company.getBussinessLocation());
			companyProfileResponse.setEmail(company.getEmail());
			companyProfileResponse.setGstNo(company.getGstNo());
			if (company.getLogo() != null) {
				companyProfileResponse.setLogo(fileGetUrl + company.getLogo());
			}
			companyProfileResponse.setMobileNumber(company.getMobileNumber());
			companyProfileResponse.setName(company.getName());
			companyProfileResponse.setPanCard(company.getPanCard());
			companyProfileResponse.setPinCode(company.getPinCode());
		
			
			if (company.getState()  > 0 && company.getState() + "" != ""
					&& company.getState() != null) {
				State state = this.stateRepository.findById(company.getState())
						.orElseThrow(() -> new ResourceNotFoundException(
								"State Id " + company.getState() + " is not exist"));
				companyProfileResponse.setState(state.getStateTitle());
			} else {
				companyProfileResponse.setState("");
			}

			if (company.getDistrict() > 0 && company.getDistrict() + "" != ""
					&& company.getDistrict() != null) {
				District district = this.districtRepository.findById(company.getDistrict())
						.orElseThrow(() -> new ResourceNotFoundException(
								"District Id " + company.getDistrict() + " is not exist"));
				companyProfileResponse.setDistrict(district.getDistrictTitle());
			} else {
				companyProfileResponse.setDistrict("");
			}

			if (company.getCity() > 0 && company.getCity() + "" != ""
					&& company.getCity() != null) {
				City city = this.cityRepository.findById(company.getCity())
						.orElseThrow(() -> new ResourceNotFoundException(
								"City Id " + company.getCity() + " is not exist"));
				companyProfileResponse.setCity(city.getName());
			} else {
				companyProfileResponse.setCity("");
			}
			
			
			
			companyList.add(companyProfileResponse);
		});
		companyListWithPagination.setCompanyList(companyList);
		companyListWithPagination.setLastPage(findAll.isLast());
		companyListWithPagination.setPageNumber(findAll.getNumber());
		companyListWithPagination.setPageSize(findAll.getSize());
		companyListWithPagination.setTotalElement(findAll.getTotalElements());
		companyListWithPagination.setTotalPages(findAll.getTotalPages());
		return companyListWithPagination;
	}

	@Override
	public CompanyDetailResponse getCompanyDetailById(long companyId) {
		Company company = this.companyRepository.findById(companyId).orElseThrow(() -> new ResourceNotFoundException(
				"Company not exist"));
		
		CompanyDetailResponse companyDetailResponse = new CompanyDetailResponse();
		companyDetailResponse.setAddress(company.getAddress());
		companyDetailResponse.setBussinessLocation(company.getBussinessLocation());
		companyDetailResponse.setEmail(company.getEmail());
		companyDetailResponse.setGstNo(company.getGstNo());
		companyDetailResponse.setId(company.getId());
		companyDetailResponse.setInterviewLink(company.getInterviewLink());
		companyDetailResponse.setLogo(fileGetUrl+company.getLogo());
		companyDetailResponse.setMobileNumber(company.getMobileNumber());
		companyDetailResponse.setName(company.getName());
		companyDetailResponse.setPanCard(company.getPanCard());
		companyDetailResponse.setPinCode(company.getPinCode());
	
		if (company.getState()  > 0 && company.getState() + "" != ""
				&& company.getState() != null) {
			State state = this.stateRepository.findById(company.getState())
					.orElseThrow(() -> new ResourceNotFoundException(
							"State Id " + company.getState() + " is not exist"));
			companyDetailResponse.setState(state.getStateTitle());
		} else {
			companyDetailResponse.setState("---");
		}

		if (company.getDistrict() > 0 && company.getDistrict() + "" != ""
				&& company.getDistrict() != null) {
			District district = this.districtRepository.findById(company.getDistrict())
					.orElseThrow(() -> new ResourceNotFoundException(
							"District Id " + company.getDistrict() + " is not exist"));
			companyDetailResponse.setDistrict(district.getDistrictTitle());
		} else {
			companyDetailResponse.setDistrict("---");
		}

		if (company.getCity() > 0 && company.getCity() + "" != ""
				&& company.getCity() != null) {
			City city = this.cityRepository.findById(company.getCity())
					.orElseThrow(() -> new ResourceNotFoundException(
							"City Id " + company.getCity() + " is not exist"));
			companyDetailResponse.setCity(city.getName());
		} else {
			companyDetailResponse.setCity("----");
		}
		
		
		return companyDetailResponse;
	}

	@Override
	public Map<String, String> updateInterViewLink(UpdateCompanyInterViewLink companyInterViewLink) {
		Company company = this.companyRepository.findById(companyInterViewLink.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException(
				"Company not exist"));
		company.setInterviewLink(companyInterViewLink.getInterViewLink());
		 this.companyRepository.save(company);
		 Map<String, String> response = new HashMap<String, String>();
		 response.put("Status", "Interview Link Updated Successfully");
		return response;
	}
	

}
