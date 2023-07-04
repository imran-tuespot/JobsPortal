package com.tuespotsolutions.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.CandidateEducation;
import com.tuespotsolutions.entity.CandidateExperience;
import com.tuespotsolutions.entity.CandidateLanguages;
import com.tuespotsolutions.entity.CandidateProjects;
import com.tuespotsolutions.entity.CandidateSalary;
import com.tuespotsolutions.entity.City;
import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.CompanyInboxNotification;
import com.tuespotsolutions.entity.District;
import com.tuespotsolutions.entity.Job;
import com.tuespotsolutions.entity.JobApplied;
import com.tuespotsolutions.entity.JobWorkMode;
import com.tuespotsolutions.entity.ShortListCompanyInbox;
import com.tuespotsolutions.entity.State;
import com.tuespotsolutions.models.CandidateLanguage;
import com.tuespotsolutions.models.CandidateProjectDetail;
import com.tuespotsolutions.models.CandidateRegistrationResponse;
import com.tuespotsolutions.models.CandidateSalaryDetail;
import com.tuespotsolutions.models.CompanyInboxNotificationDetails;
import com.tuespotsolutions.models.CompanyInboxNotificationAll;
import com.tuespotsolutions.models.CompanyInboxNotificationAllWithPagination;
import com.tuespotsolutions.models.CompanyInboxNotificationTrashList;
import com.tuespotsolutions.models.CompanyInboxNotificationResponseShortlisted;
import com.tuespotsolutions.models.CompanyInboxPagination;
import com.tuespotsolutions.models.CompanyInboxShortListWithPagination;
import com.tuespotsolutions.models.CompanyInboxTrashListWithPagination;
import com.tuespotsolutions.models.CompanyResponse;
import com.tuespotsolutions.models.JobResponse;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.CityRepository;
import com.tuespotsolutions.repository.CompanyInboxNotificationRepository;
import com.tuespotsolutions.repository.CompanyRepository;
import com.tuespotsolutions.repository.DistrictRepository;
import com.tuespotsolutions.repository.JobAppliedRepository;
import com.tuespotsolutions.repository.JobRepository;
import com.tuespotsolutions.repository.JobWorkModeRepository;
import com.tuespotsolutions.repository.StateRepository;
import com.tuespotsolutions.service.CompanyInboxNotificationService;
import com.tuespotsolutions.util.ConstantConfiguration;

@Service
public class CompanyInboxNotificationServiceImpl implements CompanyInboxNotificationService {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobAppliedRepository jobAppliedRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private CompanyInboxNotificationRepository companyInboxNotificationRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private JobWorkModeRepository jobWorkModeRepository;

	@Autowired
	private CityRepository cityRepository;

	@Value("${file.upload.path}")
	private String fileUploadUrl;

	@Value("${file.get.url}")
	private String downloadResume;

	@Override
	public CompanyInboxNotificationAllWithPagination getNotificationList(long companyId, Integer page, Integer size) {

		Pageable pageable = PageRequest.of(page, size);
		
		
		List<CompanyInboxNotification> companyInboxNotification = this.companyInboxNotificationRepository
				.findByCompanyId(companyId);
		List<CompanyInboxNotificationAll> companyInboxNotificationResponses = new ArrayList<CompanyInboxNotificationAll>();
		companyInboxNotification.forEach(data -> {

			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_RECIVED)) {

				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Candidate candidate = this.candidateRepository.findById(data.getCandidateId())
						.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

				CompanyInboxNotificationAll companyInboxNotificationResponse = new CompanyInboxNotificationAll();

				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				try {
					Date createdOn = timeStamp.parse(data.getCreatedOn() + "");
					Date date = new Date();
					String format = timeStamp.format(date);
					Date parse = timeStamp.parse(format);
					long createdTime = createdOn.getTime();
					long nowTime = parse.getTime();
					long activeHours = nowTime - createdTime;

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
					long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
					if (diffInHours < 1) {
						companyInboxNotificationResponse.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						companyInboxNotificationResponse.setActiveHour(diffInHours + " hour");
					} else {
						companyInboxNotificationResponse.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				companyInboxNotificationResponse.setCandidateName(candidate.getName());
				companyInboxNotificationResponse.setJobName(job.getTitle());

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

				companyInboxNotificationResponse.setDescription(candidate.getDescription());
				companyInboxNotificationResponse.setLocation(candidate.getAddress() + ", " + city.getName() + " "
						+ district.getDistrictTitle() + ", " + state.getStateTitle() + ", " + candidate.getPinCode());
				companyInboxNotificationResponse.setResume(this.downloadResume + candidate.getResume());
				companyInboxNotificationResponse.setCandidateImage(this.downloadResume + candidate.getImage());
				companyInboxNotificationResponse.setId(data.getId());
				companyInboxNotificationResponse.setStatus(data.getStatus());
				companyInboxNotificationResponse.setStatusSeen(data.getStatusSeen());
				companyInboxNotificationResponses.add(companyInboxNotificationResponse);
			}
		});
		
		

		Collections.sort(companyInboxNotificationResponses, new Comparator<CompanyInboxNotificationAll>() {

			@Override
			public int compare(CompanyInboxNotificationAll o1, CompanyInboxNotificationAll o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});
		PageImpl<CompanyInboxNotificationAll> pageImplAllNotification = new PageImpl<CompanyInboxNotificationAll>(companyInboxNotificationResponses, pageable, companyInboxNotificationResponses.size());
		CompanyInboxNotificationAllWithPagination allWithPagination = new CompanyInboxNotificationAllWithPagination();
		allWithPagination.setAllNotifications(pageImplAllNotification.getContent());
		allWithPagination.setLastPage(pageImplAllNotification.isLast());
		allWithPagination.setPageNumber(pageImplAllNotification.getNumber());
		allWithPagination.setPageSize(pageImplAllNotification.getSize());
		allWithPagination.setTotalElement(pageImplAllNotification.getTotalElements());
		allWithPagination.setTotalPages(pageImplAllNotification.getTotalPages());

		return allWithPagination;
	}

	@Override
	public List<CompanyInboxNotificationAll> getLatestNotification(long companyId) {

		List<CompanyInboxNotification> companyInboxNotification = this.companyInboxNotificationRepository
				.getLatestNotification(companyId);
		List<CompanyInboxNotificationAll> companyInboxNotificationResponses = new ArrayList<CompanyInboxNotificationAll>();
		companyInboxNotification.forEach(data -> {
			Job job = this.jobRepository.findById(data.getJobId())
					.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

			Candidate candidate = this.candidateRepository.findById(data.getCandidateId())
					.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

			CompanyInboxNotificationAll companyInboxNotificationResponse = new CompanyInboxNotificationAll();

			TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
			SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			timeStamp.setTimeZone(istTimeZone);
			try {
				Date createdOn = timeStamp.parse(data.getCreatedOn() + "");
				Date date = new Date();
				String format = timeStamp.format(date);
				Date parse = timeStamp.parse(format);
				long createdTime = createdOn.getTime();
				long nowTime = parse.getTime();
				long activeHours = nowTime - createdTime;

				long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
				long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
				long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
				long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
				if (diffInHours < 1) {
					companyInboxNotificationResponse.setActiveHour(diffInMinutes + " mins");
				} else if (diffInHours >= 1 && diffInHours < 24) {
					companyInboxNotificationResponse.setActiveHour(diffInHours + " hour");
				} else {
					companyInboxNotificationResponse.setActiveHour(diffInDays + " day");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			companyInboxNotificationResponse.setCandidateName(candidate.getName());
			companyInboxNotificationResponse.setCandidateImage(downloadResume + candidate.getImage());
			companyInboxNotificationResponse.setJobName(job.getTitle());
			companyInboxNotificationResponse.setLocation(null);
			companyInboxNotificationResponse.setId(data.getId());
			companyInboxNotificationResponses.add(companyInboxNotificationResponse);
		});

		Collections.sort(companyInboxNotificationResponses, new Comparator<CompanyInboxNotificationAll>() {

			@Override
			public int compare(CompanyInboxNotificationAll o1, CompanyInboxNotificationAll o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		return companyInboxNotificationResponses;
	}

	@Override
	public CompanyInboxNotificationDetails getNotificationDetails(long notificationId) {

		CompanyInboxNotification companyInboxNotification = this.companyInboxNotificationRepository
				.findById(notificationId)
				.orElseThrow(() -> new ResourceNotFoundException("CompanyInboxNotification Not Found"));

		Candidate candidate = this.candidateRepository.findById(companyInboxNotification.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

		Job job = this.jobRepository.findById(companyInboxNotification.getJobId())
				.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

		Company company = this.companyRepository.findById(companyInboxNotification.getCompanyId())
				.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

		CompanyInboxNotificationDetails companyInboxNotificationDetails = new CompanyInboxNotificationDetails();

		companyInboxNotificationDetails.setNotificationId(companyInboxNotification.getId());
		companyInboxNotificationDetails.setNotifcationDate(companyInboxNotification.getCreatedOn() + "");

		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);
		try {
			Date createdOn = timeStamp.parse(companyInboxNotification.getCreatedOn() + "");
			Date date = new Date();
			String format = timeStamp.format(date);
			Date parse = timeStamp.parse(format);
			long createdTime = createdOn.getTime();
			long nowTime = parse.getTime();
			long activeHours = nowTime - createdTime;

			long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
			long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
			long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
			long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
			if (diffInHours < 1) {
				companyInboxNotificationDetails.setNotificationActiveHours(diffInMinutes + " mins");
			} else if (diffInHours >= 1 && diffInHours < 24) {
				companyInboxNotificationDetails.setNotificationActiveHours(diffInHours + " hour");
			} else {
				companyInboxNotificationDetails.setNotificationActiveHours(diffInDays + " day");
			}

			companyInboxNotificationDetails.setId(candidate.getId());
			companyInboxNotificationDetails.setEmail(candidate.getEmail());
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

			companyInboxNotificationDetails.setCompanyId(company.getId());
			companyInboxNotificationDetails.setCandidateId(candidate.getId());
			companyInboxNotificationDetails.setLocation(candidate.getAddress() + " " + city.getName() + ", dist. "
					+ district.getDistrictTitle() + ", state : " + state.getStateTitle());
			companyInboxNotificationDetails.setMobileNumber(candidate.getMobileNumber());
			companyInboxNotificationDetails.setName(candidate.getName());
			companyInboxNotificationDetails.setPinCode(candidate.getPinCode());
			companyInboxNotificationDetails.setResume(downloadResume + candidate.getResume());
			companyInboxNotificationDetails.setImage(downloadResume + candidate.getImage());
			companyInboxNotificationDetails.setCity(city.getName());
			companyInboxNotificationDetails.setCityId(city.getId());
			companyInboxNotificationDetails.setDateOfBirth(candidate.getDateOfBirth());
			companyInboxNotificationDetails.setDistrict(district.getDistrictTitle());
			companyInboxNotificationDetails.setDistrictId(district.getDistrictId());
			companyInboxNotificationDetails.setId(candidate.getId());
			companyInboxNotificationDetails.setPanCard(candidate.getPanCard());
			companyInboxNotificationDetails.setState(state.getStateTitle());
			companyInboxNotificationDetails.setStateId(state.getStateId());
			companyInboxNotificationDetails.setAboutYourSelf(candidate.getDescription());

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
				companyInboxNotificationDetails.setCandidateEducation(educationList);
			});

			List<CandidateExperience> candidateExperiences = candidate.getCandidateExperiences();
			List<com.tuespotsolutions.models.CandidateExperience> candidateExerienceList = new ArrayList<com.tuespotsolutions.models.CandidateExperience>();
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
				candidateExerienceList.add(candidateExperience);
				companyInboxNotificationDetails.setCandidateExperience(candidateExerienceList);
			});

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
				companyInboxNotificationDetails.setCandidateLanguage(languageList);
			});

			List<CandidateProjects> candidateProjects = candidate.getCandidateProjects();
			List<CandidateProjectDetail> projectsList = new ArrayList<CandidateProjectDetail>();
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
				projectsList.add(candidateProjectDetail);
				companyInboxNotificationDetails.setCandidateProjectDetail(projectsList);
			});

			List<CandidateSalary> candidateSalaries = candidate.getCandidateSalaries();
			List<CandidateSalaryDetail> salaryList = new ArrayList<CandidateSalaryDetail>();
			candidateSalaries.forEach(salary -> {
				CandidateSalaryDetail candidateSalaryDetail = new CandidateSalaryDetail();
				candidateSalaryDetail.setId(salary.getId());
				candidateSalaryDetail.setCurrentCtc(salary.getCurrentCtc());
				candidateSalaryDetail.setExpectedCtc(salary.getExpectedCtc());
				candidateSalaryDetail.setNoticePeriod(salary.getNoticePeriod());
				salaryList.add(candidateSalaryDetail);
				companyInboxNotificationDetails.setCandidateSalary(salaryList);
			});

			JobResponse jobResponse = new JobResponse();
			jobResponse.setDepartment(job.getDepartment());
			jobResponse.setDescription(job.getDescription());
			jobResponse.setExperience(job.getExperience());
			jobResponse.setId(job.getId());
			jobResponse.setJobType(job.getJobType());
			jobResponse.setLocation(job.getLocation());
			jobResponse.setSkills(job.getSkills());
			jobResponse.setStatus(job.isStatus());
			jobResponse.setTitle(job.getTitle());

			CompanyResponse companyResponse = new CompanyResponse();
			companyResponse.setEmail(company.getEmail());
			companyResponse.setName(company.getName());
			companyResponse.setId(company.getId());
			jobResponse.setCompany(companyResponse);

			jobResponse.setLogo(this.downloadResume + company.getLogo());
			companyInboxNotificationDetails.setJobResponse(jobResponse);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return companyInboxNotificationDetails;
	}

	@Override
	public void setSeenStatusOnNotifiation(long notificationId) {
		CompanyInboxNotification notification = this.companyInboxNotificationRepository.findById(notificationId)
				.orElseThrow(() -> new ResourceNotFoundException("CompanyInboxNotification Not Found"));
		notification.setStatusSeen(ConstantConfiguration.NOTIFICATION_SEEN);
		this.companyInboxNotificationRepository.save(notification);
	}

	@Override
	public List<CompanyInboxNotificationAll> shortListCompanyInbox(List<Long> notificationId) {
		List<CompanyInboxNotification> inboxList = this.companyInboxNotificationRepository.findAllById(notificationId);
		inboxList.forEach(inbox -> {

			if (inbox.getStatus()
					.equalsIgnoreCase(ConstantConfiguration.NOTIFICATION_SHORTLISTED)) {

			} else {

				java.util.Date utilDate = new java.util.Date();
				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				inbox.setModifiedOn(utilDate);
				inbox.setCreatedOn(utilDate);
				inbox.setStatus(ConstantConfiguration.NOTIFICATION_SHORTLISTED);
			}

		});

		List<CompanyInboxNotification> saveAll = this.companyInboxNotificationRepository.saveAll(inboxList);

		List<CompanyInboxNotificationAll> companyInboxNotificationResponses = new ArrayList<CompanyInboxNotificationAll>();
		saveAll.forEach(data -> {

			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_RECIVED)) {

				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Candidate candidate = this.candidateRepository.findById(data.getCandidateId())
						.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

				CompanyInboxNotificationAll companyInboxNotificationResponse = new CompanyInboxNotificationAll();

				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				try {
					Date createdOn = timeStamp.parse(data.getCreatedOn() + "");
					Date date = new Date();
					String format = timeStamp.format(date);
					Date parse = timeStamp.parse(format);
					long createdTime = createdOn.getTime();
					long nowTime = parse.getTime();
					long activeHours = nowTime - createdTime;

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
					long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
					if (diffInHours < 1) {
						companyInboxNotificationResponse.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						companyInboxNotificationResponse.setActiveHour(diffInHours + " hour");
					} else {
						companyInboxNotificationResponse.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				companyInboxNotificationResponse.setCandidateName(candidate.getName());
				companyInboxNotificationResponse.setJobName(job.getTitle());

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

				companyInboxNotificationResponse.setDescription(candidate.getDescription());
				companyInboxNotificationResponse.setLocation(candidate.getAddress() + ", " + city.getName() + " "
						+ district.getDistrictTitle() + ", " + state.getStateTitle() + ", " + candidate.getPinCode());
				companyInboxNotificationResponse.setResume(this.downloadResume + candidate.getResume());
				companyInboxNotificationResponse.setCandidateImage(this.downloadResume + candidate.getImage());
				companyInboxNotificationResponse.setId(data.getId());
				companyInboxNotificationResponse.setStatus(data.getStatus());
				companyInboxNotificationResponse.setStatusSeen(data.getStatusSeen());
				companyInboxNotificationResponses.add(companyInboxNotificationResponse);
			}
		});

		Collections.sort(companyInboxNotificationResponses, new Comparator<CompanyInboxNotificationAll>() {

			@Override
			public int compare(CompanyInboxNotificationAll o1, CompanyInboxNotificationAll o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		return companyInboxNotificationResponses;
	}

	@Override
	public List<CompanyInboxNotificationAll> trashListCompanyInbox(List<Long> notificationId) {

		notificationId.forEach(id -> {
			System.err.println("notification : " + this.companyInboxNotificationRepository.findById(id).get());
		});

		List<CompanyInboxNotification> inboxList = this.companyInboxNotificationRepository.findAllById(notificationId);
		List<CompanyInboxNotification> updateInbox = new ArrayList<CompanyInboxNotification>();
		inboxList.forEach(inbox -> {

			if (inbox.getStatus().equalsIgnoreCase(ConstantConfiguration.NOTIFICATION_TRASH)) {

			} else {

				java.util.Date utilDate = new java.util.Date();
				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				inbox.setModifiedOn(utilDate);
				inbox.setCreatedOn(utilDate);
				inbox.setStatus(ConstantConfiguration.NOTIFICATION_TRASH);
				//updateInbox.add(inbox);
				this.companyInboxNotificationRepository.save(inbox);
			}

		});

		List<CompanyInboxNotification> saveAll = this.companyInboxNotificationRepository.saveAll(updateInbox);

		List<CompanyInboxNotificationAll> companyInboxNotificationResponses = new ArrayList<CompanyInboxNotificationAll>();
		saveAll.forEach(data -> {

			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_RECIVED)) {

				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Candidate candidate = this.candidateRepository.findById(data.getCandidateId())
						.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

				CompanyInboxNotificationAll companyInboxNotificationResponse = new CompanyInboxNotificationAll();

				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				try {
					Date createdOn = timeStamp.parse(data.getCreatedOn() + "");
					Date date = new Date();
					String format = timeStamp.format(date);
					Date parse = timeStamp.parse(format);
					long createdTime = createdOn.getTime();
					long nowTime = parse.getTime();
					long activeHours = nowTime - createdTime;

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
					long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
					if (diffInHours < 1) {
						companyInboxNotificationResponse.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						companyInboxNotificationResponse.setActiveHour(diffInHours + " hour");
					} else {
						companyInboxNotificationResponse.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				companyInboxNotificationResponse.setCandidateName(candidate.getName());
				companyInboxNotificationResponse.setJobName(job.getTitle());

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

				companyInboxNotificationResponse.setDescription(candidate.getDescription());
				companyInboxNotificationResponse.setLocation(candidate.getAddress() + ", " + city.getName() + " "
						+ district.getDistrictTitle() + ", " + state.getStateTitle() + ", " + candidate.getPinCode());
				companyInboxNotificationResponse.setResume(this.downloadResume + candidate.getResume());
				companyInboxNotificationResponse.setCandidateImage(this.downloadResume + candidate.getImage());
				companyInboxNotificationResponse.setId(data.getId());
				companyInboxNotificationResponse.setStatus(data.getStatus());
				companyInboxNotificationResponse.setStatusSeen(data.getStatusSeen());
				companyInboxNotificationResponses.add(companyInboxNotificationResponse);
			}
		});

		Collections.sort(companyInboxNotificationResponses, new Comparator<CompanyInboxNotificationAll>() {

			@Override
			public int compare(CompanyInboxNotificationAll o1, CompanyInboxNotificationAll o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		return companyInboxNotificationResponses;
	}

	@Override
	public CompanyInboxNotificationAllWithPagination getShortListCompanyInbox(long companyId, Integer page, Integer size) {
		List<CompanyInboxNotification> companyInboxNotification = this.companyInboxNotificationRepository
				.findByCompanyId(companyId);
		List<CompanyInboxNotificationAll> companyInboxNotificationResponses = new ArrayList<CompanyInboxNotificationAll>();
		companyInboxNotification.forEach(data -> {

			if (data.getStatus().equalsIgnoreCase(ConstantConfiguration.NOTIFICATION_SHORTLISTED)) {

				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Candidate candidate = this.candidateRepository.findById(data.getCandidateId())
						.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

				CompanyInboxNotificationAll companyInboxNotificationResponse = new CompanyInboxNotificationAll();

				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				try {
					Date createdOn = timeStamp.parse(data.getCreatedOn() + "");
					Date date = new Date();
					String format = timeStamp.format(date);
					Date parse = timeStamp.parse(format);
					long createdTime = createdOn.getTime();
					long nowTime = parse.getTime();
					long activeHours = nowTime - createdTime;

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
					long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
					if (diffInHours < 1) {
						companyInboxNotificationResponse.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						companyInboxNotificationResponse.setActiveHour(diffInHours + " hour");
					} else {
						companyInboxNotificationResponse.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				companyInboxNotificationResponse.setCandidateName(candidate.getName());
				companyInboxNotificationResponse.setJobName(job.getTitle());

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

				companyInboxNotificationResponse.setDescription(candidate.getDescription());
				companyInboxNotificationResponse.setLocation(candidate.getAddress() + ", " + city.getName() + " "
						+ district.getDistrictTitle() + ", " + state.getStateTitle() + ", " + candidate.getPinCode());
				companyInboxNotificationResponse.setResume(this.downloadResume + candidate.getResume());
				companyInboxNotificationResponse.setCandidateImage(this.downloadResume + candidate.getImage());
				companyInboxNotificationResponse.setId(data.getId());
				companyInboxNotificationResponse.setStatus(data.getStatus());
				companyInboxNotificationResponse.setStatusSeen(data.getStatusSeen());
				companyInboxNotificationResponses.add(companyInboxNotificationResponse);
			}
		});

		Collections.sort(companyInboxNotificationResponses, new Comparator<CompanyInboxNotificationAll>() {

			@Override
			public int compare(CompanyInboxNotificationAll o1, CompanyInboxNotificationAll o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		Pageable pageable = PageRequest.of(page, size);
		
		PageImpl<CompanyInboxNotificationAll> pageImplAllNotification = new PageImpl<CompanyInboxNotificationAll>(companyInboxNotificationResponses, pageable, companyInboxNotificationResponses.size());
		CompanyInboxNotificationAllWithPagination allWithPagination = new CompanyInboxNotificationAllWithPagination();
		allWithPagination.setAllNotifications(pageImplAllNotification.getContent());
		allWithPagination.setLastPage(pageImplAllNotification.isLast());
		allWithPagination.setPageNumber(pageImplAllNotification.getNumber());
		allWithPagination.setPageSize(pageImplAllNotification.getSize());
		allWithPagination.setTotalElement(pageImplAllNotification.getTotalElements());
		allWithPagination.setTotalPages(pageImplAllNotification.getTotalPages());

		return allWithPagination;
	}

	@Override
	public CompanyInboxNotificationAllWithPagination getTrashListCompanyInbox(long companyId, Integer page, Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<CompanyInboxNotification> companyInboxNotification = this.companyInboxNotificationRepository
				.findByCompanyId(companyId);
		List<CompanyInboxNotificationAll> companyInboxNotificationResponses = new ArrayList<CompanyInboxNotificationAll>();
		companyInboxNotification.forEach(data -> {

			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_TRASH)) {

				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Candidate candidate = this.candidateRepository.findById(data.getCandidateId())
						.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

				CompanyInboxNotificationAll companyInboxNotificationResponse = new CompanyInboxNotificationAll();

				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				try {
					Date createdOn = timeStamp.parse(data.getCreatedOn() + "");
					Date date = new Date();
					String format = timeStamp.format(date);
					Date parse = timeStamp.parse(format);
					long createdTime = createdOn.getTime();
					long nowTime = parse.getTime();
					long activeHours = nowTime - createdTime;

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
					long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
					if (diffInHours < 1) {
						companyInboxNotificationResponse.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						companyInboxNotificationResponse.setActiveHour(diffInHours + " hour");
					} else {
						companyInboxNotificationResponse.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				companyInboxNotificationResponse.setCandidateName(candidate.getName());
				companyInboxNotificationResponse.setJobName(job.getTitle());

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

				companyInboxNotificationResponse.setDescription(candidate.getDescription());
				companyInboxNotificationResponse.setLocation(candidate.getAddress() + ", " + city.getName() + " "
						+ district.getDistrictTitle() + ", " + state.getStateTitle() + ", " + candidate.getPinCode());
				companyInboxNotificationResponse.setResume(this.downloadResume + candidate.getResume());
				companyInboxNotificationResponse.setCandidateImage(this.downloadResume + candidate.getImage());
				companyInboxNotificationResponse.setId(data.getId());
				companyInboxNotificationResponse.setStatus(data.getStatus());
				companyInboxNotificationResponse.setStatusSeen(data.getStatusSeen());
				companyInboxNotificationResponses.add(companyInboxNotificationResponse);
			}
		});

		Collections.sort(companyInboxNotificationResponses, new Comparator<CompanyInboxNotificationAll>() {

			@Override
			public int compare(CompanyInboxNotificationAll o1, CompanyInboxNotificationAll o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		PageImpl<CompanyInboxNotificationAll> pageImplAllNotification = new PageImpl<CompanyInboxNotificationAll>(companyInboxNotificationResponses, pageable, companyInboxNotificationResponses.size());
		CompanyInboxNotificationAllWithPagination allWithPagination = new CompanyInboxNotificationAllWithPagination();
		allWithPagination.setAllNotifications(pageImplAllNotification.getContent());
		allWithPagination.setLastPage(pageImplAllNotification.isLast());
		allWithPagination.setPageNumber(pageImplAllNotification.getNumber());
		allWithPagination.setPageSize(pageImplAllNotification.getSize());
		allWithPagination.setTotalElement(pageImplAllNotification.getTotalElements());
		allWithPagination.setTotalPages(pageImplAllNotification.getTotalPages());

		return allWithPagination;
	}

	@Override
	public CompanyInboxPagination getAllComapanyNotifications(long companyId, Integer page, Integer size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page, size);
		
		List<CompanyInboxNotification> findByCompanyId = this.companyInboxNotificationRepository
				.findByCompanyId(companyId);

		List<CompanyInboxNotificationAll> allNotifications = new ArrayList<CompanyInboxNotificationAll>();
		List<CompanyInboxNotificationResponseShortlisted> shortListedNotifications = new ArrayList<CompanyInboxNotificationResponseShortlisted>();
		List<CompanyInboxNotificationTrashList> trashNotication = new ArrayList<CompanyInboxNotificationTrashList>();
		findByCompanyId.forEach(data -> {
			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_TRASH)) {

				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Candidate candidate = this.candidateRepository.findById(data.getCandidateId())
						.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

				CompanyInboxNotificationTrashList trashNoticationData = new CompanyInboxNotificationTrashList();

				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				try {
					Date createdOn = timeStamp.parse(data.getCreatedOn() + "");
					Date date = new Date();
					String format = timeStamp.format(date);
					Date parse = timeStamp.parse(format);
					long createdTime = createdOn.getTime();
					long nowTime = parse.getTime();
					long activeHours = nowTime - createdTime;

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
					long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
					if (diffInHours < 1) {
						trashNoticationData.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						trashNoticationData.setActiveHour(diffInHours + " hour");
					} else {
						trashNoticationData.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				trashNoticationData.setCandidateName(candidate.getName());
				trashNoticationData.setJobName(job.getTitle());

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

				trashNoticationData.setDescription(candidate.getDescription());
				trashNoticationData.setLocation(candidate.getAddress() + ", " + city.getName() + " "
						+ district.getDistrictTitle() + ", " + state.getStateTitle() + ", " + candidate.getPinCode());
				trashNoticationData.setResume(this.downloadResume + candidate.getResume());
				trashNoticationData.setCandidateImage(this.downloadResume + candidate.getImage());
				trashNoticationData.setId(data.getId());
				trashNoticationData.setStatus(data.getStatus());
				trashNoticationData.setStatusSeen(data.getStatusSeen());
				trashNotication.add(trashNoticationData);
			}

			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_RECIVED)) {

				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Candidate candidate = this.candidateRepository.findById(data.getCandidateId())
						.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

				CompanyInboxNotificationAll companyInboxNotificationAll = new CompanyInboxNotificationAll();

				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				try {
					Date createdOn = timeStamp.parse(data.getCreatedOn() + "");
					Date date = new Date();
					String format = timeStamp.format(date);
					Date parse = timeStamp.parse(format);
					long createdTime = createdOn.getTime();
					long nowTime = parse.getTime();
					long activeHours = nowTime - createdTime;

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
					long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
					if (diffInHours < 1) {
						companyInboxNotificationAll.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						companyInboxNotificationAll.setActiveHour(diffInHours + " hour");
					} else {
						companyInboxNotificationAll.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				companyInboxNotificationAll.setCandidateName(candidate.getName());
				companyInboxNotificationAll.setJobName(job.getTitle());

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

				companyInboxNotificationAll.setDescription(candidate.getDescription());
				companyInboxNotificationAll.setLocation(candidate.getAddress() + ", " + city.getName() + " "
						+ district.getDistrictTitle() + ", " + state.getStateTitle() + ", " + candidate.getPinCode());
				companyInboxNotificationAll.setResume(this.downloadResume + candidate.getResume());
				companyInboxNotificationAll.setCandidateImage(this.downloadResume + candidate.getImage());
				companyInboxNotificationAll.setId(data.getId());
				companyInboxNotificationAll.setStatus(data.getStatus());
				companyInboxNotificationAll.setStatusSeen(data.getStatusSeen());
				allNotifications.add(companyInboxNotificationAll);
			}

			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_SHORTLISTED)) {

				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Candidate candidate = this.candidateRepository.findById(data.getCandidateId())
						.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

				CompanyInboxNotificationResponseShortlisted shortlistNotifications = new CompanyInboxNotificationResponseShortlisted();

				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				try {
					Date createdOn = timeStamp.parse(data.getCreatedOn() + "");
					Date date = new Date();
					String format = timeStamp.format(date);
					Date parse = timeStamp.parse(format);
					long createdTime = createdOn.getTime();
					long nowTime = parse.getTime();
					long activeHours = nowTime - createdTime;

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
					long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
					if (diffInHours < 1) {
						shortlistNotifications.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						shortlistNotifications.setActiveHour(diffInHours + " hour");
					} else {
						shortlistNotifications.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				shortlistNotifications.setCandidateName(candidate.getName());
				shortlistNotifications.setJobName(job.getTitle());

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

				shortlistNotifications.setDescription(candidate.getDescription());
				shortlistNotifications.setLocation(candidate.getAddress() + ", " + city.getName() + " "
						+ district.getDistrictTitle() + ", " + state.getStateTitle() + ", " + candidate.getPinCode());
				shortlistNotifications.setResume(this.downloadResume + candidate.getResume());
				shortlistNotifications.setCandidateImage(this.downloadResume + candidate.getImage());
				shortlistNotifications.setId(data.getId());
				shortlistNotifications.setStatus(data.getStatus());
				shortlistNotifications.setStatusSeen(data.getStatusSeen());
				shortListedNotifications.add(shortlistNotifications);
			}

		});

		Collections.sort(allNotifications, new Comparator<CompanyInboxNotificationAll>() {

			@Override
			public int compare(CompanyInboxNotificationAll o1, CompanyInboxNotificationAll o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		Collections.sort(shortListedNotifications, new Comparator<CompanyInboxNotificationResponseShortlisted>() {

			@Override
			public int compare(CompanyInboxNotificationResponseShortlisted o1,
					CompanyInboxNotificationResponseShortlisted o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		Collections.sort(trashNotication, new Comparator<CompanyInboxNotificationTrashList>() {

			@Override
			public int compare(CompanyInboxNotificationTrashList o1, CompanyInboxNotificationTrashList o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		PageImpl<CompanyInboxNotificationAll> pageImplAllNotification = new PageImpl<CompanyInboxNotificationAll>(allNotifications, pageable, allNotifications.size());
		CompanyInboxNotificationAllWithPagination allWithPagination = new CompanyInboxNotificationAllWithPagination();
		allWithPagination.setAllNotifications(pageImplAllNotification.getContent());
		allWithPagination.setLastPage(pageImplAllNotification.isLast());
		allWithPagination.setPageNumber(pageImplAllNotification.getNumber());
		allWithPagination.setPageSize(pageImplAllNotification.getSize());
		allWithPagination.setTotalElement(pageImplAllNotification.getTotalElements());
		allWithPagination.setTotalPages(pageImplAllNotification.getTotalPages());
		
		
		PageImpl<CompanyInboxNotificationResponseShortlisted> pageImplShortList = new PageImpl<CompanyInboxNotificationResponseShortlisted>(shortListedNotifications, pageable, shortListedNotifications.size());
		CompanyInboxShortListWithPagination  shortListedNotification = new CompanyInboxShortListWithPagination();
		shortListedNotification.setShortListNotifications(pageImplShortList.getContent());
		shortListedNotification.setLastPage(pageImplShortList.isLast());
		shortListedNotification.setPageNumber(pageImplShortList.getNumber());
		shortListedNotification.setPageSize(pageImplShortList.getSize());
		shortListedNotification.setTotalElement(pageImplShortList.getTotalElements());
		shortListedNotification.setTotalPages(pageImplShortList.getTotalPages());
		
		
		PageImpl<CompanyInboxNotificationTrashList> pageImplTrashList = new PageImpl<CompanyInboxNotificationTrashList>(trashNotication, pageable, trashNotication.size());
		CompanyInboxTrashListWithPagination trashListNotification = new CompanyInboxTrashListWithPagination();
		trashListNotification.setTrashNotifications(pageImplTrashList.getContent());
		trashListNotification.setLastPage(pageImplTrashList.isLast());
		trashListNotification.setPageNumber(pageImplTrashList.getNumber());
		trashListNotification.setPageSize(pageImplTrashList.getSize());
		trashListNotification.setTotalElement(pageImplTrashList.getTotalElements());
		trashListNotification.setTotalPages(pageImplTrashList.getTotalPages());
		
		//response
		CompanyInboxPagination companyInboxPagination = new CompanyInboxPagination();
		companyInboxPagination.setAllNotifications(allWithPagination);
		companyInboxPagination.setShortList(shortListedNotification);
		companyInboxPagination.setTrashList(trashListNotification);
		return companyInboxPagination;
	}

}
