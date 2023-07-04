package com.tuespotsolutions.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.FieldAlreadyExistException;
import com.tuespotsolutions.customexception.InvalidFileFormatException;
import com.tuespotsolutions.customexception.OtpNotSendedException;
import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.email.service.EmailServiceImpl;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.CandidateEducation;
import com.tuespotsolutions.entity.CandidateExperience;
import com.tuespotsolutions.entity.CandidateLanguages;
import com.tuespotsolutions.entity.CandidateProjects;
import com.tuespotsolutions.entity.CandidateSalary;
import com.tuespotsolutions.entity.City;
import com.tuespotsolutions.entity.District;
import com.tuespotsolutions.entity.OnlinePeopleStatus;
import com.tuespotsolutions.entity.RegistrationOtp;
import com.tuespotsolutions.entity.Roles;
import com.tuespotsolutions.entity.State;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.models.CandiateListWithPagination;
import com.tuespotsolutions.models.CandidateLanguage;
import com.tuespotsolutions.models.CandidateProjectDetail;
import com.tuespotsolutions.models.CandidateRegistrationResponse;
import com.tuespotsolutions.models.CandidateRequest;
import com.tuespotsolutions.models.CandidateSalaryDetail;
import com.tuespotsolutions.models.SearchPeopleDetail;
import com.tuespotsolutions.models.SearchedPeopleResponse;
import com.tuespotsolutions.repository.CandidateEducationRepository;
import com.tuespotsolutions.repository.CandidateExperienceRepository;
import com.tuespotsolutions.repository.CandidateLanguagesRepository;
import com.tuespotsolutions.repository.CandidateProjectsRepository;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.CandidateSalaryRepository;
import com.tuespotsolutions.repository.CityRepository;
import com.tuespotsolutions.repository.CompanyRepository;
import com.tuespotsolutions.repository.DistrictRepository;
import com.tuespotsolutions.repository.OnlinePeopleStatusRepository;
import com.tuespotsolutions.repository.RegistrationOtpRepository;
import com.tuespotsolutions.repository.RolesRepository;
import com.tuespotsolutions.repository.StateRepository;
import com.tuespotsolutions.repository.UserRepository;
import com.tuespotsolutions.service.CandidateRegistrationService;
import com.tuespotsolutions.specifications.SearchPeopleSpecification;
import com.tuespotsolutions.util.ConstantConfiguration;
import com.tuespotsolutions.util.FileUpload;
import com.tuespotsolutions.util.OtpSender;

@Service
public class CandidateRegistrationServiceImpl implements CandidateRegistrationService {

	Logger logger = LoggerFactory.getLogger(CandidateRegistrationServiceImpl.class);

	@Autowired
	private CandidateRepository candidateRepository;

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
	private CandidateEducationRepository candidateEducationRepository;

	@Autowired
	private CandidateExperienceRepository candidateExperienceRepository;

	@Autowired
	private CandidateLanguagesRepository candidateLanguagesRepository;

	@Autowired
	private CandidateProjectsRepository candidateProjectsRepository;

	@Autowired
	private CandidateSalaryRepository candidateSalaryRepository;

	@Autowired
	RegistrationOtpRepository registrationOtpRepository;

	@Autowired
	EmailServiceImpl emailServiceImpl;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	OnlinePeopleStatusRepository onlinePeopleStatusRepository;

	@Value("${file.upload.path}")
	private String fileUploadUrl;

	@Value("${file.get.url}")
	private String downloadResume;

	

	@Override 
	public Map<String, String> registerCandidate(CandidateRequest candidateRequest) {

		@SuppressWarnings("unchecked")
		Map<String, String> map = new HashedMap();
		if (candidateRepository.existsByEmail(candidateRequest.getEmail())) {
			Optional<Candidate> existCandidate = candidateRepository.findByEmail(candidateRequest.getEmail());
			if (existCandidate.isPresent()) {
				if (existCandidate.get().isStatus()) {
					logger.error("line no 117 FieldAlreadyExistException, message : Email is already exis "
							+ candidateRequest.getEmail());
					throw new FieldAlreadyExistException("Email is already exist : " + candidateRequest.getEmail());
				} else {
					Optional<User> findByUserTypeAndTypeId = userRepository
							.findByUserTypeAndTypeId(ConstantConfiguration.CANDIDATE, existCandidate.get().getId());
					if (findByUserTypeAndTypeId.isPresent()) {
						userRepository.delete(findByUserTypeAndTypeId.get());
						Optional<RegistrationOtp> findByUserIdAndUserType = registrationOtpRepository
								.findByUserIdAndUserType(existCandidate.get().getId(), ConstantConfiguration.CANDIDATE);
						registrationOtpRepository.delete(findByUserIdAndUserType.get());
						candidateRepository.delete(existCandidate.get());
					}
				}

			}
		}

		if (candidateRepository.existsByMobileNumber(candidateRequest.getMobileNumber())) {
			Optional<Candidate> existCandidate = candidateRepository
					.findByMobileNumber(candidateRequest.getMobileNumber());
			if (existCandidate.isPresent()) {
				if (existCandidate.get().isStatus()) {
					logger.error("line no 127 FieldAlreadyExistException, message : Mobile Number Already exist "
							+ candidateRequest.getMobileNumber());
					throw new FieldAlreadyExistException(
							"Mobile Number Already exist : " + candidateRequest.getMobileNumber());
				} else {
					Optional<User> findByUserTypeAndTypeId = userRepository
							.findByUserTypeAndTypeId(ConstantConfiguration.CANDIDATE, existCandidate.get().getId());
					if (findByUserTypeAndTypeId.isPresent()) {
						userRepository.delete(findByUserTypeAndTypeId.get());
						Optional<RegistrationOtp> findByUserIdAndUserType = registrationOtpRepository
								.findByUserIdAndUserType(existCandidate.get().getId(), ConstantConfiguration.CANDIDATE);
						registrationOtpRepository.delete(findByUserIdAndUserType.get());
						candidateRepository.delete(existCandidate.get());
					}
				}
			}
		}

		if (candidateRepository.existsByUsername(candidateRequest.getUserName())) {
			Optional<Candidate> existCandidate = candidateRepository.findByUsername(candidateRequest.getEmail());
			if (existCandidate.isPresent()) {
				if (existCandidate.get().isStatus()) {
					logger.error("line no 137 FieldAlreadyExistException, message : Username Already exist "
							+ candidateRequest.getUserName());
					throw new FieldAlreadyExistException("Username Already exist : " + candidateRequest.getUserName());
				} else {
					Optional<User> findByUserTypeAndTypeId = userRepository
							.findByUserTypeAndTypeId(ConstantConfiguration.CANDIDATE, existCandidate.get().getId());
					if (findByUserTypeAndTypeId.isPresent()) {
						userRepository.delete(findByUserTypeAndTypeId.get());
						Optional<RegistrationOtp> findByUserIdAndUserType = registrationOtpRepository
								.findByUserIdAndUserType(existCandidate.get().getId(), ConstantConfiguration.CANDIDATE);
						registrationOtpRepository.delete(findByUserIdAndUserType.get());
						candidateRepository.delete(existCandidate.get());
					}
				}
			}
		}

		if (companyRepository.existsByUsername(candidateRequest.getUserName())) {
			logger.error("line no 145 FieldAlreadyExistException, message : Username Already exist "
					+ candidateRequest.getUserName());
			throw new FieldAlreadyExistException("Username Already exist : " + candidateRequest.getUserName());
		}

		java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

		int otp = new Random().nextInt(900000) + 100000;
		String sendOtpSms = OtpSender.sendOtpSms(candidateRequest.getMobileNumber(), "Job Solutions", otp + "");

		if (!sendOtpSms.isEmpty()) {

			Optional<Candidate> existCandidate = candidateRepository.findByEmail(candidateRequest.getEmail());
			if (existCandidate.isPresent()) {
				if (!existCandidate.get().isStatus()) {

					if (sendOtpSms != null) {
						Optional<RegistrationOtp> registrationOtp = this.registrationOtpRepository
								.findByUserIdAndUserType(existCandidate.get().getId(), ConstantConfiguration.CANDIDATE);
						if (registrationOtp.isPresent()) {
							registrationOtp.get().setOtp(otp + "");
							registrationOtp.get().setOtpRefrenceId(sendOtpSms);
							registrationOtp.get().setCreatedOn(sqlDate);
							registrationOtp.get().setModifiedOn(sqlDate);
							RegistrationOtp otpResp = registrationOtpRepository.save(registrationOtp.get());
							map.put("message", "You have recieved your otp on this number : "
									+ existCandidate.get().getMobileNumber());
							map.put("otpId", otpResp.getId() + "");

						}
					}

				}
			} else {
				if (sendOtpSms != null) {
					Candidate candidate = new Candidate();
					candidate.setName(candidateRequest.getName());
					candidate.setAddress(candidateRequest.getAddress());

					if (candidateRequest.getStateId() > 0 && candidateRequest.getStateId() + "" != ""
							&& candidateRequest.getStateId() != null) {
						State state = this.stateRepository.findById(candidateRequest.getStateId())
								.orElseThrow(() -> new ResourceNotFoundException(
										"State Id " + candidateRequest.getStateId() + " is not exist"));
						candidate.setState(state.getStateId());
					} else {
						candidate.setState(0);
					}

					if (candidateRequest.getDistrictId() > 0 && candidateRequest.getDistrictId() + "" != ""
							&& candidateRequest.getDistrictId() != null) {
						District district = this.districtRepository.findById(candidateRequest.getDistrictId())
								.orElseThrow(() -> new ResourceNotFoundException(
										"District Id " + candidateRequest.getDistrictId() + " is not exist"));
						candidate.setDistrict(district.getDistrictId());
					} else {
						candidate.setDistrict(0);
					}

					if (candidateRequest.getCityId() > 0 && candidateRequest.getCityId() + "" != ""
							&& candidateRequest.getCityId() != null) {
						City city = this.cityRepository.findById(candidateRequest.getCityId())
								.orElseThrow(() -> new ResourceNotFoundException(
										"City Id " + candidateRequest.getCityId() + " is not exist"));
						candidate.setCity(city.getId());
					} else {
						candidate.setCity(0);
					}

					candidate.setDateOfBirth(candidateRequest.getDateOfBirth());

					candidate.setCreatedOn(sqlDate);
					candidate.setModifiedOn(sqlDate);

					candidate.setMobileNumber(candidateRequest.getMobileNumber());
					candidate.setUsername(candidateRequest.getUserName());
					candidate.setEmail(candidateRequest.getEmail());
					candidate.setPanCard(candidateRequest.getPanCard());
					candidate.setPinCode(candidateRequest.getPinCode());
					candidate.setProfileHeadline(candidateRequest.getProfileHeadline());;
					if (candidateRequest.getResume() != null && candidateRequest.getResume() != ""
							&& candidateRequest.getResume().contains("data:application")) {
						String uploadFile = FileUpload.uploadResume(candidateRequest.getResume(), fileUploadUrl);
						candidate.setResume(uploadFile);
					} else {
						throw new InvalidFileFormatException("Only pdf and docx extensions are valid");
					}

					if (candidateRequest.getImage() != null && candidateRequest.getImage() != ""
							&& candidateRequest.getImage().contains("data:image")) {
						String uploadFile = FileUpload.uploadFile(candidateRequest.getImage(), fileUploadUrl);
						candidate.setImage(uploadFile);
					} else {
						throw new InvalidFileFormatException("Only pdf and docx extensions are valid");
					}

					candidate.setStatus(false);
					Candidate save = this.candidateRepository.save(candidate);

					if (save != null) {

						// ===== save candidate education ====//
						CandidateEducation candidateEducation = new CandidateEducation();
						candidateEducation.setName(candidateRequest.getEducation());
						candidateEducation.setCourse(candidateRequest.getCourse());
						candidateEducation.setCourseDuration(candidateRequest.getCourseDuration());
						candidateEducation.setCourseType(candidateRequest.getCourseType());
						candidateEducation.setGradingSystem(candidateRequest.getGradingSystem());
						candidateEducation.setMarks(candidateRequest.getMarks());
						candidateEducation.setSpecialization(candidateRequest.getSpecialization());
						candidateEducation.setUniversity(candidateRequest.getUniversity());
						candidateEducation.setCandidate(save);
						this.candidateEducationRepository.save(candidateEducation);
						// ===== ./ save candidate education ./ ====//

						// ===== save candidate experience ====//
						CandidateExperience candidateExperience = new CandidateExperience();
						candidateExperience.setCompanyName(candidateRequest.getCompanyName());
						candidateExperience.setCtc(candidateRequest.getCtc());
						candidateExperience.setCurrent(candidateRequest.isCurrent());
						candidateExperience.setDesignation(candidateRequest.getDescription());
						candidateExperience.setEmploymentType(candidateRequest.getEmploymentType());
						candidateExperience.setEndDate(candidateRequest.getEndDate());
						candidateExperience.setJoingDate(candidateRequest.getJoingDate());
						candidateExperience.setJobProfile(candidateRequest.getJobProfile());
						candidateExperience.setCandidate(save);
						this.candidateExperienceRepository.save(candidateExperience);
						// ===== ./ save candidate experience ./ ====//

						// ===== save candidate Languages ====//
						CandidateLanguages candidateLanguages = new CandidateLanguages();
						candidateLanguages.setLevel(candidateRequest.getLevel());
						candidateLanguages.setName(candidateRequest.getLanguage());
						candidateLanguages.setRead(candidateRequest.isRead());
						candidateLanguages.setWrite(candidateRequest.isWrite());
						candidateLanguages.setSpeak(candidateRequest.isSpeak());
						candidateLanguages.setCandidate(save);
						this.candidateLanguagesRepository.save(candidateLanguages);
						// ===== ./ save candidate Languages ./ ====//

						// ===== save candidate Projects ====//
						CandidateProjects candidateProjects = new CandidateProjects();
						candidateProjects.setClient(candidateRequest.getClient());
						candidateProjects.setDescription(candidateRequest.getDescription());
						candidateProjects.setEndDate(candidateRequest.getProjectEndDate());
						candidateProjects.setLocation(candidateRequest.getLocation());
						candidateProjects.setNatureOfEmployement(candidateRequest.getNatureOfEmployement());
						candidateProjects.setProjectStatus(candidateRequest.isProjectStatus());
						candidateProjects.setRole(candidateRequest.getRole());
						candidateProjects.setRoleDescription(candidateRequest.getRoleDescription());
						candidateProjects.setSkillSet(candidateRequest.getSkillSet());
						candidateProjects.setStartDate(candidateRequest.getProjectStartDate());
						candidateProjects.setTeamSize(candidateRequest.getTeamSize());
						candidateProjects.setTitle(candidateRequest.getTitle());
						candidateProjects.setCandidate(save);
						this.candidateProjectsRepository.save(candidateProjects);
						// ===== ./ save candidate Projects ./ ====//

						// ===== save candidate Salary ====//
						CandidateSalary candidateSalary = new CandidateSalary();
						candidateSalary.setCurrentCtc(candidateRequest.getCurrentCtc());
						candidateSalary.setExpectedCtc(candidateRequest.getExpectedCtc());
						candidateSalary.setNoticePeriod(candidateRequest.getNoticePeriod());
						candidateSalary.setCandidate(save);
						this.candidateSalaryRepository.save(candidateSalary);
						// ===== ./ save candidate Salary ./ ====//

						// ===== save User ====//

						// save user
						User user = new User();
						user.setApplicantName(save.getName());
						user.setUsername(save.getUsername());
						user.setEmail(save.getEmail());
						user.setMobileNumber(save.getMobileNumber());
						Roles role = this.rolesRepository.findById(2L)
								.orElseThrow(() -> new ResourceNotFoundException("Role Id " + 2 + " is not exist"));
						Set<Roles> roles = new HashSet<Roles>();
						roles.add(role);
						user.setRoles(roles);
						String encodedPassword = passwordEncoder.encode(candidateRequest.getPassword());
						user.setPassword(encodedPassword);
						user.setCreatedOn(sqlDate);
						user.setModifiedOn(sqlDate);
						user.setUserType(ConstantConfiguration.CANDIDATE);
						user.setTypeId(save.getId());
						user.setStatus(false);
						this.userRepository.save(user);

						// ===== ./ save User ./ ====//

						RegistrationOtp registrationOtp = new RegistrationOtp();
						registrationOtp.setOtp(otp + "");
						registrationOtp.setOtpRefrenceId(sendOtpSms);
						registrationOtp.setUserId(save.getId());
						registrationOtp.setUserType(ConstantConfiguration.CANDIDATE);
						registrationOtp.setCreatedOn(sqlDate);
						registrationOtp.setModifiedOn(sqlDate);
						RegistrationOtp otpResp = registrationOtpRepository.save(registrationOtp);

						map.put("message", "You have recieved your otp on this number : " + save.getMobileNumber());
						map.put("otpId", otpResp.getId() + "");

					}

				}
			}
		} else {
			logger.error("line no 344 OtpNotSendedException");
			throw new OtpNotSendedException(
					"Kindly Check your mobile number we are not able to send otp on this numner : "
							+ candidateRequest.getMobileNumber());
		}
		return map;
	}

	@Override
	public CandidateRegistrationResponse getCandidate(long candidateId) {

		Candidate candidate = this.candidateRepository.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

		CandidateRegistrationResponse candidateRegistrationResponse = new CandidateRegistrationResponse();
		candidateRegistrationResponse.setId(candidate.getId());
		candidateRegistrationResponse.setEmail(candidate.getEmail());
		City city = new City();
		if (candidate.getCity() > 0) {
			city = this.cityRepository.findById(candidate.getCity())
					.orElseThrow(() -> new ResourceNotFoundException("City Not Found"));
		}

		District district = new District();
		if (candidate.getState() > 0) {
			district = this.districtRepository.findById(candidate.getDistrict())
					.orElseThrow(() -> new ResourceNotFoundException("District Not Found"));
		}

		State state = new State();
		if (candidate.getState() > 0) {
			state = this.stateRepository.findById(candidate.getState())
					.orElseThrow(() -> new ResourceNotFoundException("State Not Found"));
		}

		candidateRegistrationResponse.setLocation(candidate.getAddress() + " " + city.getName() + ", dist. "
				+ district.getDistrictTitle() + ", state : " + state.getStateTitle());
		candidateRegistrationResponse.setMobileNumber(candidate.getMobileNumber());
		candidateRegistrationResponse.setName(candidate.getName());
		candidateRegistrationResponse.setPinCode(candidate.getPinCode());
		candidateRegistrationResponse.setResume(downloadResume + candidate.getResume());
		candidateRegistrationResponse.setImage(downloadResume + candidate.getImage());
		candidateRegistrationResponse.setCity(city.getName());
		candidateRegistrationResponse.setCityId(city.getId());
		candidateRegistrationResponse.setDateOfBirth(candidate.getDateOfBirth());
		candidateRegistrationResponse.setDistrict(district.getDistrictTitle());
		candidateRegistrationResponse.setDistrictId(district.getDistrictId());
		candidateRegistrationResponse.setId(candidate.getId());
		candidateRegistrationResponse.setPanCard(candidate.getPanCard());
		candidateRegistrationResponse.setState(state.getStateTitle());
		candidateRegistrationResponse.setStateId(state.getStateId());
		candidateRegistrationResponse.setAboutYourSelf(candidate.getDescription());
		List<CandidateEducation> candidateEducation = candidate.getCandidateEducation();
		candidateEducation.forEach(education -> {
			com.tuespotsolutions.models.CandidateEducation candidateEdu = new com.tuespotsolutions.models.CandidateEducation();
			candidateEdu.setId(education.getId());
			candidateEdu.setName(education.getName());
			candidateEdu.setCourse(education.getCourse());
			candidateEdu.setCourseDuration(education.getCourseDuration());
			candidateEdu.setCourseType(education.getCourseType());
			candidateEdu.setGradingSystem(education.getGradingSystem());
			candidateEdu.setMarks(education.getMarks());
			candidateEdu.setSpecialization(education.getSpecialization());
			candidateEdu.setUniversity(education.getUniversity());
			candidateRegistrationResponse.setCandidateEducation(candidateEdu);
		});

		List<CandidateExperience> candidateExperiences = candidate.getCandidateExperiences();
		candidateExperiences.forEach(experience -> {
			com.tuespotsolutions.models.CandidateExperience candidateExperience = new com.tuespotsolutions.models.CandidateExperience();
			candidateExperience.setCompanyName(experience.getCompanyName());
			candidateExperience.setCtc(experience.getCtc());
			candidateExperience.setCurrent(true);
			candidateExperience.setDesignation(experience.getDesignation());
			candidateExperience.setEmploymentType(experience.getEmploymentType());
			candidateExperience.setEndDate(experience.getEndDate());
			candidateExperience.setId(experience.getId());
			candidateExperience.setJobProfile(experience.getJobProfile());
			candidateExperience.setJoingDate(experience.getJoingDate());
			candidateRegistrationResponse.setCandidateExperience(candidateExperience);
		});

		List<CandidateLanguages> candidateLanguages = candidate.getCandidateLanguages();
		candidateLanguages.forEach(language -> {
			CandidateLanguage candidateLanguage = new CandidateLanguage();
			candidateLanguage.setId(language.getId());
			candidateLanguage.setName(language.getName());
			candidateLanguage.setLevel(language.getLevel());
			candidateLanguage.setRead(language.isRead());
			candidateLanguage.setSpeak(language.isSpeak());
			candidateLanguage.setWrite(language.isWrite());
			candidateRegistrationResponse.setCandidateLanguage(candidateLanguage);
		});

		List<CandidateProjects> candidateProjects = candidate.getCandidateProjects();
		candidateProjects.forEach(projects -> {
			CandidateProjectDetail candidateProjectDetail = new CandidateProjectDetail();
			candidateProjectDetail.setId(projects.getId());
			candidateProjectDetail.setTitle(projects.getTitle());
			candidateProjectDetail.setClient(projects.getClient());
			candidateProjectDetail.setDescription(projects.getDescription());
			candidateProjectDetail.setLocation(projects.getLocation());

			candidateProjectDetail.setNatureOfEmployement(projects.getNatureOfEmployement());
			candidateProjectDetail.setProjectEndDate(projects.getEndDate());
			candidateProjectDetail.setProjectStartDate(projects.getStartDate());
			candidateProjectDetail.setProjectStatus(true);
			candidateProjectDetail.setRole(projects.getRole());
			candidateProjectDetail.setRoleDescription(projects.getRoleDescription());
			candidateProjectDetail.setSkillSet(projects.getSkillSet());
			candidateProjectDetail.setTeamSize(projects.getTeamSize());
			candidateRegistrationResponse.setCandidateProjectDetail(candidateProjectDetail);
		});

		List<CandidateSalary> candidateSalaries = candidate.getCandidateSalaries();
		candidateSalaries.forEach(salary -> {
			CandidateSalaryDetail candidateSalaryDetail = new CandidateSalaryDetail();
			candidateSalaryDetail.setId(salary.getId());
			candidateSalaryDetail.setCurrentCtc(salary.getCurrentCtc());
			candidateSalaryDetail.setExpectedCtc(salary.getExpectedCtc());
			candidateSalaryDetail.setNoticePeriod(salary.getNoticePeriod());
			candidateRegistrationResponse.setCandidateSalary(candidateSalaryDetail);
		});

		return candidateRegistrationResponse;
	}

	@Override
	public CandiateListWithPagination getCandidateList(Integer pageNumber, Integer pageSize) {

		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));

		Page<Candidate> findAll = this.candidateRepository.findAll(pageable);
		List<Candidate> content = findAll.getContent();
		List<CandidateRegistrationResponse> contentList = new ArrayList<CandidateRegistrationResponse>();
		content.forEach(candidate -> {
			CandidateRegistrationResponse candidateRegistrationResponse = new CandidateRegistrationResponse();
			candidateRegistrationResponse.setId(candidate.getId());
			candidateRegistrationResponse.setEmail(candidate.getEmail());
			City city = new City();
			if (candidate.getCity() > 0) {
				city = this.cityRepository.findById(candidate.getCity())
						.orElseThrow(() -> new ResourceNotFoundException("City Not Found"));
			}

			District district = new District();
			if (candidate.getState() > 0) {
				district = this.districtRepository.findById(candidate.getDistrict())
						.orElseThrow(() -> new ResourceNotFoundException("District Not Found"));
			}

			State state = new State();
			if (candidate.getState() > 0) {
				state = this.stateRepository.findById(candidate.getState())
						.orElseThrow(() -> new ResourceNotFoundException("State Not Found"));
			}
			candidateRegistrationResponse.setLocation(
					city.getName() + ", dist. " + district.getDistrictTitle() + ", state : " + state.getStateTitle());
			candidateRegistrationResponse.setMobileNumber(candidate.getMobileNumber());
			candidateRegistrationResponse.setName(candidate.getName());
			candidateRegistrationResponse.setPinCode(candidate.getPinCode());
			candidateRegistrationResponse.setResume(downloadResume + candidate.getResume());

			contentList.add(candidateRegistrationResponse);
		});

		CandiateListWithPagination candiateListWithPagination = new CandiateListWithPagination();
		candiateListWithPagination.setCandidates(contentList);
		candiateListWithPagination.setLastPage(findAll.isLast());
		candiateListWithPagination.setPageNumber(findAll.getNumber());
		candiateListWithPagination.setPageSize(findAll.getSize());
		candiateListWithPagination.setTotalElement(findAll.getTotalElements());
		candiateListWithPagination.setTotalPages(findAll.getTotalPages());
		return candiateListWithPagination;
	}

	@Override
	public Map<String, String> updateCandidate(CandidateRegistrationResponse candidateRequest) {

		try {

			Candidate candidate = this.candidateRepository.findById(candidateRequest.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Exist"));

			candidate.setName(candidateRequest.getName());

			if (candidateRequest.getStateId() > 0 && candidateRequest.getStateId() + "" != ""
					&& candidateRequest.getStateId() != null) {
				State state = this.stateRepository.findById(candidateRequest.getStateId())
						.orElseThrow(() -> new ResourceNotFoundException(
								"State Id " + candidateRequest.getStateId() + " is not exist"));
				candidate.setState(state.getStateId());
			} else {
				candidate.setState(0);
			}

			if (candidateRequest.getDistrictId() > 0 && candidateRequest.getDistrictId() + "" != ""
					&& candidateRequest.getDistrictId() != null) {
				District district = this.districtRepository.findById(candidateRequest.getDistrictId())
						.orElseThrow(() -> new ResourceNotFoundException(
								"District Id " + candidateRequest.getDistrictId() + " is not exist"));
				candidate.setDistrict(district.getDistrictId());
			} else {
				candidate.setDistrict(0);
			}

			if (candidateRequest.getCityId() > 0 && candidateRequest.getCityId() + "" != ""
					&& candidateRequest.getCityId() != null) {
				City city = this.cityRepository.findById(candidateRequest.getCityId())
						.orElseThrow(() -> new ResourceNotFoundException(
								"City Id " + candidateRequest.getCityId() + " is not exist"));
				candidate.setCity(city.getId());
			} else {
				candidate.setCity(0);
			}

			candidate.setDateOfBirth(candidateRequest.getDateOfBirth());

			java.util.Date utilDate = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

			candidate.setModifiedOn(sqlDate);

			candidate.setMobileNumber(candidateRequest.getMobileNumber());
			candidate.setEmail(candidateRequest.getEmail());
			candidate.setPanCard(candidateRequest.getPanCard());
			candidate.setPinCode(candidateRequest.getPinCode());

			if (candidateRequest.getResume() != null && candidateRequest.getResume() != ""
					&& candidateRequest.getResume().contains("data:application")) {
				String uploadFile = FileUpload.uploadResume(candidateRequest.getResume(), fileUploadUrl);
				candidate.setResume(uploadFile);
			}
			if (candidateRequest.getImage() != null && candidateRequest.getImage() != ""
					&& candidateRequest.getImage().contains("data:image")) {
				String uploadFile = FileUpload.uploadFile(candidateRequest.getImage(), fileUploadUrl);
				candidate.setImage(uploadFile);
			}
			candidate.setDescription(candidateRequest.getAboutYourSelf());
			Candidate save = this.candidateRepository.save(candidate);
			CandidateEducation candidateEducation = new CandidateEducation();
			candidateEducation.setId(candidateRequest.getCandidateEducation().getId());
			candidateEducation.setName(candidateRequest.getCandidateEducation().getName());
			candidateEducation.setCourse(candidateRequest.getCandidateEducation().getCourse());
			candidateEducation.setCourseDuration(candidateRequest.getCandidateEducation().getCourseDuration());
			candidateEducation.setCourseType(candidateRequest.getCandidateEducation().getCourseType());
			candidateEducation.setGradingSystem(candidateRequest.getCandidateEducation().getGradingSystem());
			candidateEducation.setMarks(candidateRequest.getCandidateEducation().getMarks());
			candidateEducation.setSpecialization(candidateRequest.getCandidateEducation().getSpecialization());
			candidateEducation.setUniversity(candidateRequest.getCandidateEducation().getUniversity());
			candidateEducation.setCandidate(save);
			this.candidateEducationRepository.save(candidateEducation);

			CandidateExperience candidateExperience = new CandidateExperience();
			candidateExperience.setCompanyName(candidateRequest.getCandidateExperience().getCompanyName());
			candidateExperience.setCtc(candidateRequest.getCandidateExperience().getCtc());
			candidateExperience.setCurrent(true);
			candidateExperience.setDesignation(candidateRequest.getCandidateExperience().getDesignation());
			candidateExperience.setEmploymentType(candidateRequest.getCandidateExperience().getEmploymentType());
			candidateExperience.setEndDate(candidateRequest.getCandidateExperience().getEndDate());
			candidateExperience.setId(candidateRequest.getCandidateExperience().getId());
			candidateExperience.setJobProfile(candidateRequest.getCandidateExperience().getJobProfile());
			candidateExperience.setJoingDate(candidateRequest.getCandidateExperience().getJoingDate());
			candidateExperience.setCandidate(save);
			this.candidateExperienceRepository.save(candidateExperience);

			CandidateLanguages candidateLanguages = new CandidateLanguages();
			candidateLanguages.setId(candidateRequest.getCandidateLanguage().getId());
			candidateLanguages.setName(candidateRequest.getCandidateLanguage().getName());
			candidateLanguages.setLevel(candidateRequest.getCandidateLanguage().getLevel());
			candidateLanguages.setRead(candidateRequest.getCandidateLanguage().isRead());
			candidateLanguages.setSpeak(candidateRequest.getCandidateLanguage().isSpeak());
			candidateLanguages.setWrite(candidateRequest.getCandidateLanguage().isWrite());
			candidateLanguages.setCandidate(save);
			this.candidateLanguagesRepository.save(candidateLanguages);

			CandidateProjects candidateProjects = new CandidateProjects();
			candidateProjects.setId(candidateRequest.getCandidateProjectDetail().getId());
			candidateProjects.setTitle(candidateRequest.getCandidateProjectDetail().getTitle());
			candidateProjects.setClient(candidateRequest.getCandidateProjectDetail().getClient());
			candidateProjects.setDescription(candidateRequest.getCandidateProjectDetail().getDescription());
			candidateProjects.setLocation(candidateRequest.getCandidateProjectDetail().getLocation());
			candidateProjects
					.setNatureOfEmployement(candidateRequest.getCandidateProjectDetail().getNatureOfEmployement());

			candidateProjects.setEndDate(candidateRequest.getCandidateProjectDetail().getProjectEndDate());
			candidateProjects.setStartDate(candidateRequest.getCandidateProjectDetail().getProjectStartDate());
			candidateProjects.setProjectStatus(true);
			candidateProjects.setRole(candidateRequest.getCandidateProjectDetail().getRole());
			candidateProjects.setRoleDescription(candidateRequest.getCandidateProjectDetail().getRoleDescription());
			candidateProjects.setSkillSet(candidateRequest.getCandidateProjectDetail().getSkillSet());
			candidateProjects.setTeamSize(candidateRequest.getCandidateProjectDetail().getTeamSize());
			candidateProjects.setCandidate(save);
			this.candidateProjectsRepository.save(candidateProjects);

			CandidateSalary candidateSalary = new CandidateSalary();
			candidateSalary.setId(candidateRequest.getCandidateSalary().getId());
			candidateSalary.setCurrentCtc(candidateRequest.getCandidateSalary().getCurrentCtc());
			candidateSalary.setExpectedCtc(candidateRequest.getCandidateSalary().getExpectedCtc());
			candidateSalary.setNoticePeriod(candidateRequest.getCandidateSalary().getNoticePeriod());
			candidateSalary.setCandidate(save);
			this.candidateSalaryRepository.save(candidateSalary);

		} catch (Exception e) {
			e.printStackTrace();
		}

		@SuppressWarnings("unchecked")
		Map<String, String> map = new HashedMap();
		map.put("message", "Profile Updated SuccessFully");

		return map;
	}

	@Override
	public List<SearchedPeopleResponse> searchedPeople(String profileHeadline) {
		Specification<Candidate> specification = Specification
				.where(SearchPeopleSpecification.containsProfileHeadline(profileHeadline));
		List<Candidate> findAll = this.candidateRepository.findAll(specification);
		
		
		
		
		List<SearchedPeopleResponse> peopleResponses = new ArrayList<SearchedPeopleResponse>();
		findAll.forEach(people -> {
			
			User user = this.userRepository.findByUserTypeAndTypeId("CANDIDATE", people.getId()).orElseThrow(
					() -> new ResourceNotFoundException("User Not Found"));
			
			OnlinePeopleStatus onlinePeopleStatus = this.onlinePeopleStatusRepository.findByUserId(user.getId()).orElseThrow(
					() -> new ResourceNotFoundException("User Not Found"));
			
			long time = onlinePeopleStatus.getLastSeen().getTime();
			Date now = new Date();
			TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
			SimpleDateFormat  timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			timeStamp.setTimeZone(istTimeZone);
			String format = timeStamp.format(now.getTime());
			long current = now.getTime();
			long diff = (current - time)/1000;
			
			System.err.println("time : "+diff);
					
			if(diff <= 10) {
		
			SearchedPeopleResponse peopleResponse = new SearchedPeopleResponse();

			String location = null;
			if (people.getState() > 0 && people.getState() + "" != "" && people.getState() != null) {
				State state = this.stateRepository.findById(people.getState()).orElseThrow(
						() -> new ResourceNotFoundException("State Id " + people.getState() + " is not exist"));
				location = state.getStateTitle();
			}

			if (people.getDistrict() > 0 && people.getDistrict() + "" != "" && people.getDistrict() != null) {
				District district = this.districtRepository.findById(people.getDistrict()).orElseThrow(
						() -> new ResourceNotFoundException("District Id " + people.getDistrict() + " is not exist"));
				location += ", " + district.getDistrictId();
			}

			if (people.getCity() > 0 && people.getCity() + "" != "" && people.getCity() != null) {
				City city = this.cityRepository.findById(people.getCity()).orElseThrow(
						() -> new ResourceNotFoundException("City Id " + people.getCity() + " is not exist"));
				location += ", " + city.getId();
			}
			peopleResponse.setId(people.getId());
			peopleResponse.setLastSeen(diff+" second last seen");
			peopleResponse.setProfileImage(downloadResume + people.getImage());
			peopleResponse.setLocation(location + ", " + people.getPinCode());
			peopleResponse.setName(people.getName());
			peopleResponse.setProfileHeadline(people.getProfileHeadline());
			peopleResponses.add(peopleResponse);
			
		}

		});
		return peopleResponses;
	}

	@Override
	public SearchPeopleDetail searchedpeopleDetailById(Long candidateId) {
		

		
		
		Candidate candidate = this.candidateRepository.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));
	
		
		City city = new City();
		if (candidate.getCity() > 0) {
			city = this.cityRepository.findById(candidate.getCity())
					.orElseThrow(() -> new ResourceNotFoundException("City Not Found"));
		}

		District district = new District();
		if (candidate.getState() > 0) {
			district = this.districtRepository.findById(candidate.getDistrict())
					.orElseThrow(() -> new ResourceNotFoundException("District Not Found"));
		}

		State state = new State();
		if (candidate.getState() > 0) {
			state = this.stateRepository.findById(candidate.getState())
					.orElseThrow(() -> new ResourceNotFoundException("State Not Found"));
		}

		SearchPeopleDetail candidateRegistrationResponse = new SearchPeopleDetail();

		candidateRegistrationResponse.setLocation(candidate.getAddress() + " " + city.getName() + ", dist. "
				+ district.getDistrictTitle() + ", state : " + state.getStateTitle());
		candidateRegistrationResponse.setMobileNumber(candidate.getMobileNumber());
		candidateRegistrationResponse.setEmail(candidate.getEmail());
		candidateRegistrationResponse.setName(candidate.getName());
		candidateRegistrationResponse.setPinCode(candidate.getPinCode());
		candidateRegistrationResponse.setResume(downloadResume + candidate.getResume());
		candidateRegistrationResponse.setImage(downloadResume + candidate.getImage());
		candidateRegistrationResponse.setCity(city.getName());
		candidateRegistrationResponse.setCityId(city.getId());
		candidateRegistrationResponse.setDateOfBirth(candidate.getDateOfBirth());
		candidateRegistrationResponse.setDistrict(district.getDistrictTitle());
		candidateRegistrationResponse.setDistrictId(district.getDistrictId());
		candidateRegistrationResponse.setId(candidate.getId());
		candidateRegistrationResponse.setPanCard(candidate.getPanCard());
		candidateRegistrationResponse.setState(state.getStateTitle());
		candidateRegistrationResponse.setStateId(state.getStateId());
		candidateRegistrationResponse.setAboutYourSelf(candidate.getDescription());
		List<CandidateEducation> candidateEducation = candidate.getCandidateEducation();

		List<com.tuespotsolutions.models.CandidateEducation> educationList = new ArrayList<com.tuespotsolutions.models.CandidateEducation>();
		candidateEducation.forEach(education -> {
			com.tuespotsolutions.models.CandidateEducation candidateEdu = new com.tuespotsolutions.models.CandidateEducation();
			candidateEdu.setId(education.getId());
			candidateEdu.setName(education.getName());
			candidateEdu.setCourse(education.getCourse());
			candidateEdu.setCourseDuration(education.getCourseDuration());
			candidateEdu.setCourseType(education.getCourseType());
			candidateEdu.setGradingSystem(education.getGradingSystem());
			candidateEdu.setMarks(education.getMarks());
			candidateEdu.setSpecialization(education.getSpecialization());
			candidateEdu.setUniversity(education.getUniversity());
			educationList.add(candidateEdu);
		});
		candidateRegistrationResponse.setCandidateEducation(educationList);

		List<CandidateExperience> candidateExperiences = candidate.getCandidateExperiences();
		List<com.tuespotsolutions.models.CandidateExperience> candidateExperienceList = new ArrayList<com.tuespotsolutions.models.CandidateExperience>();
		candidateExperiences.forEach(experience -> {
			com.tuespotsolutions.models.CandidateExperience candidateExperience = new com.tuespotsolutions.models.CandidateExperience();
			candidateExperience.setCompanyName(experience.getCompanyName());
			candidateExperience.setCtc(experience.getCtc());
			candidateExperience.setCurrent(true);
			candidateExperience.setDesignation(experience.getDesignation());
			candidateExperience.setEmploymentType(experience.getEmploymentType());
			candidateExperience.setEndDate(experience.getEndDate());
			candidateExperience.setId(experience.getId());
			candidateExperience.setJobProfile(experience.getJobProfile());
			candidateExperience.setJoingDate(experience.getJoingDate());
			candidateExperienceList.add(candidateExperience);
		});
		candidateRegistrationResponse.setCandidateExperience(candidateExperienceList);

		List<CandidateLanguages> candidateLanguages = candidate.getCandidateLanguages();
		List<CandidateLanguage> languageList = new ArrayList<CandidateLanguage>();
		candidateLanguages.forEach(language -> {
			CandidateLanguage candidateLanguage = new CandidateLanguage();
			candidateLanguage.setId(language.getId());
			candidateLanguage.setName(language.getName());
			candidateLanguage.setLevel(language.getLevel());
			candidateLanguage.setRead(language.isRead());
			candidateLanguage.setSpeak(language.isSpeak());
			candidateLanguage.setWrite(language.isWrite());
			languageList.add(candidateLanguage);
		});
		candidateRegistrationResponse.setCandidateLanguage(languageList);

		List<CandidateProjects> candidateProjects = candidate.getCandidateProjects();
		List<CandidateProjectDetail> candidateProjectDetailList = new ArrayList<CandidateProjectDetail>();
		candidateProjects.forEach(projects -> {
			CandidateProjectDetail candidateProjectDetail = new CandidateProjectDetail();
			candidateProjectDetail.setId(projects.getId());
			candidateProjectDetail.setTitle(projects.getTitle());
			candidateProjectDetail.setClient(projects.getClient());
			candidateProjectDetail.setDescription(projects.getDescription());
			candidateProjectDetail.setLocation(projects.getLocation());

			candidateProjectDetail.setNatureOfEmployement(projects.getNatureOfEmployement());
			candidateProjectDetail.setProjectEndDate(projects.getEndDate());
			candidateProjectDetail.setProjectStartDate(projects.getStartDate());
			candidateProjectDetail.setProjectStatus(true);
			candidateProjectDetail.setRole(projects.getRole());
			candidateProjectDetail.setRoleDescription(projects.getRoleDescription());
			candidateProjectDetail.setSkillSet(projects.getSkillSet());
			candidateProjectDetail.setTeamSize(projects.getTeamSize());
			candidateProjectDetailList.add(candidateProjectDetail);

		});

		candidateRegistrationResponse.setCandidateProjectDetail(candidateProjectDetailList);

		List<CandidateSalary> candidateSalaries = candidate.getCandidateSalaries();
		List<CandidateSalaryDetail> candidateSalaryDetails = new ArrayList<CandidateSalaryDetail>();
		candidateSalaries.forEach(salary -> {
			CandidateSalaryDetail candidateSalaryDetail = new CandidateSalaryDetail();
			candidateSalaryDetail.setId(salary.getId());
			candidateSalaryDetail.setCurrentCtc(salary.getCurrentCtc());
			candidateSalaryDetail.setExpectedCtc(salary.getExpectedCtc());
			candidateSalaryDetail.setNoticePeriod(salary.getNoticePeriod());
			candidateSalaryDetails.add(candidateSalaryDetail);
		});
		candidateRegistrationResponse.setCandidateSalary(candidateSalaryDetails);
		return candidateRegistrationResponse;
	}
}
