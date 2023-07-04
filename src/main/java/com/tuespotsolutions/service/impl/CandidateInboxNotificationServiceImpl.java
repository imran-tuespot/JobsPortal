package com.tuespotsolutions.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.tuespotsolutions.entity.CandidateInboxNotification;
import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.CompanyInboxNotification;
import com.tuespotsolutions.entity.Job;
import com.tuespotsolutions.entity.JobWorkMode;
import com.tuespotsolutions.models.CandidateInboxDetailResponse;
import com.tuespotsolutions.models.CandidateInboxReponseRecieved;
import com.tuespotsolutions.models.CandidateInboxReponseShortlisted;
import com.tuespotsolutions.models.CandidateInboxResponse;
import com.tuespotsolutions.models.CandidateInboxResponseWithPagination;
import com.tuespotsolutions.models.CandidateInboxShortListNotification;
import com.tuespotsolutions.models.CandidateInboxTrashNotification;
import com.tuespotsolutions.models.CandidateInpboxAllNotification;
import com.tuespotsolutions.models.CompanyInboxNotificationAll;
import com.tuespotsolutions.repository.CandidateInboxNotificationRepository;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.CompanyInboxNotificationRepository;
import com.tuespotsolutions.repository.CompanyRepository;
import com.tuespotsolutions.repository.JobAppliedRepository;
import com.tuespotsolutions.repository.JobRepository;
import com.tuespotsolutions.repository.JobWorkModeRepository;
import com.tuespotsolutions.service.CandidateInboxNotificationService;
import com.tuespotsolutions.util.ConstantConfiguration;

@Service
public class CandidateInboxNotificationServiceImpl implements CandidateInboxNotificationService {

	@Autowired
	private CompanyInboxNotificationRepository companyInboxNotificationRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobAppliedRepository jobAppliedRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private JobWorkModeRepository jobWorkModeRepository;

	@Autowired
	private CandidateInboxNotificationRepository candidateInboxNotificationRepository;

	@Value("${file.get.url}")
	private String downloadResume;

	@Value("${file.get.url}")
	private String fileGetUrl;

	@Override
	public Map<String, String> sendNotificationToCandidate(Long notificationId) {

		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);

		CompanyInboxNotification companyInboxNotification = this.companyInboxNotificationRepository
				.findById(notificationId)
				.orElseThrow(() -> new ResourceNotFoundException("Company Notification Not Found"));

		Candidate candidate = this.candidateRepository.findById(companyInboxNotification.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

		Job job = this.jobRepository.findById(companyInboxNotification.getJobId())
				.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

		Company company = this.companyRepository.findById(companyInboxNotification.getCompanyId())
				.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

		CandidateInboxNotification candidateInboxNotification = new CandidateInboxNotification();
		candidateInboxNotification.setCandidateId(candidate.getId());
		candidateInboxNotification.setCompanyId(company.getId());
		candidateInboxNotification.setJobId(job.getId());
		candidateInboxNotification.setStatus(ConstantConfiguration.NOTIFICATION_RECIVED);
		candidateInboxNotification.setStatusSeen(ConstantConfiguration.NOTIFICATION_UNSEEN);
		candidateInboxNotification.setCreatedOn(utilDate);
		candidateInboxNotification.setModifiedOn(utilDate);
		this.candidateInboxNotificationRepository.save(candidateInboxNotification);
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", "LinkShaired SuccessFully");
		return map;
	}

	@Override
	public List<CandidateInboxResponseWithPagination> getNotficationList(Integer page, Integer size, Long candidateId) {

		Pageable pageable = PageRequest.of(page, size);
		Page<CandidateInboxNotification> findByCandidateId = this.candidateInboxNotificationRepository
				.findByCandidateId(pageable, candidateId);

		List<CandidateInboxResponseWithPagination> inboxResponseWithPaginations = new ArrayList<>();
		List<CandidateInboxNotification> content = findByCandidateId.getContent();

		List<CandidateInboxResponse> candidateInboxResponses = new ArrayList<CandidateInboxResponse>();
		content.forEach(data -> {

			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_RECIVED)) {

				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Company company = this.companyRepository.findById(data.getCompanyId())
						.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

				CandidateInboxResponse candidateInboxResponse = new CandidateInboxResponse();

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
						candidateInboxResponse.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						candidateInboxResponse.setActiveHour(diffInHours + " hour");
					} else {
						candidateInboxResponse.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				candidateInboxResponse.setCompanyName(company.getName());
				candidateInboxResponse.setJobName(job.getTitle());
				candidateInboxResponse.setLocation(job.getLocation());
				candidateInboxResponse.setId(data.getId());
				candidateInboxResponse.setCompanyLogo(fileGetUrl + company.getLogo());
				candidateInboxResponse.setExperience(job.getExperience());
				System.err.println(job.getDescription());
				candidateInboxResponse.setDescription(job.getDescription());
				candidateInboxResponse.setStatus(data.getStatus());
				candidateInboxResponse.setStatusSeen(data.getStatusSeen());
				candidateInboxResponses.add(candidateInboxResponse);

			}
		});

		Collections.sort(candidateInboxResponses, new Comparator<CandidateInboxResponse>() {

			@Override
			public int compare(CandidateInboxResponse o1, CandidateInboxResponse o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		CandidateInboxResponseWithPagination candidateInboxResponseWithPagination = new CandidateInboxResponseWithPagination();
//		candidateInboxResponseWithPagination.setCandidateInboxResponsesTrash(candidateInboxResponses);
//		candidateInboxResponseWithPagination.setLastPage(findByCandidateId.isLast());
//		candidateInboxResponseWithPagination.setPageNumber(findByCandidateId.getNumber());
//		candidateInboxResponseWithPagination.setPageSize(findByCandidateId.getSize());
//		candidateInboxResponseWithPagination.setTotalElement(findByCandidateId.getTotalElements());
//		candidateInboxResponseWithPagination.setTotalPages(findByCandidateId.getTotalPages());
//		inboxResponseWithPaginations.add(candidateInboxResponseWithPagination);

		return inboxResponseWithPaginations;
	}

	@Override
	public CandidateInboxDetailResponse getCandidateInboxNotificationDetail(Long notificationId) {
		CandidateInboxNotification notification = this.candidateInboxNotificationRepository.findById(notificationId)
				.orElseThrow(() -> new ResourceNotFoundException("Notification Not Found"));

		Job job = this.jobRepository.findById(notification.getJobId())
				.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

		Company company = this.companyRepository.findById(notification.getCompanyId())
				.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

		Candidate candidate = this.candidateRepository.findById(notification.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

		CandidateInboxDetailResponse candidateInboxDetailResponse = new CandidateInboxDetailResponse();
		candidateInboxDetailResponse.setNotificationId(notification.getId());
		candidateInboxDetailResponse.setCompanyName(company.getName());
		candidateInboxDetailResponse.setDate(notification.getCreatedOn() + "");
		candidateInboxDetailResponse.setInterviewUrl(company.getInterviewLink());
		candidateInboxDetailResponse.setJobName(job.getTitle());
		candidateInboxDetailResponse.setJobType(job.getJobType());
		candidateInboxDetailResponse.setLocation(job.getLocation());
		candidateInboxDetailResponse.setCandidateName(candidate.getName());
		candidateInboxDetailResponse.setCompanyEmail(company.getEmail());
		candidateInboxDetailResponse.setCompanyLogo(fileGetUrl + company.getLogo());
		candidateInboxDetailResponse.setCompanyMobileNumber(company.getMobileNumber());
		candidateInboxDetailResponse.setExperiece(job.getExperience());
		candidateInboxDetailResponse.setSkillSet(job.getSkills());
		candidateInboxDetailResponse.setDiscription(job.getDescription());
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);
		try {
			Date createdOn = timeStamp.parse(notification.getCreatedOn() + "");
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
				candidateInboxDetailResponse.setActiveHour(diffInMinutes + " mins");
			} else if (diffInHours >= 1 && diffInHours < 24) {
				candidateInboxDetailResponse.setActiveHour(diffInHours + " hour");
			} else {
				candidateInboxDetailResponse.setActiveHour(diffInDays + " day");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return candidateInboxDetailResponse;
	}

	@Override
	public CandidateInboxResponseWithPagination getLatestNotification(Long candidateId,Integer page, Integer size) {
		List<CandidateInboxNotification> latestInboxNotification = this.candidateInboxNotificationRepository
				.getLatestInboxNotification(candidateId);
		List<CandidateInboxResponse> candidateInboxResponses = new ArrayList<CandidateInboxResponse>();
		latestInboxNotification.forEach(data -> {

			Job job = this.jobRepository.findById(data.getJobId())
					.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

			Company company = this.companyRepository.findById(data.getCompanyId())
					.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

			CandidateInboxResponse candidateInboxResponse = new CandidateInboxResponse();
			candidateInboxResponse.setId(data.getId());
			candidateInboxResponse.setStatus(data.getStatus());
			candidateInboxResponse.setStatusSeen(data.getStatusSeen());
			candidateInboxResponse.setDescription(job.getDescription());
			candidateInboxResponse.setExperience(job.getExperience());
			candidateInboxResponse.setCompanyName(company.getName());
			candidateInboxResponse.setJobName(job.getTitle());
			candidateInboxResponse.setLocation(job.getLocation());
			candidateInboxResponse.setCompanyLogo(fileGetUrl + company.getLogo());
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
					candidateInboxResponse.setActiveHour(diffInMinutes + " mins");
				} else if (diffInHours >= 1 && diffInHours < 24) {
					candidateInboxResponse.setActiveHour(diffInHours + " hour");
				} else {
					candidateInboxResponse.setActiveHour(diffInDays + " day");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			candidateInboxResponses.add(candidateInboxResponse);
		});

		Collections.sort(candidateInboxResponses, new Comparator<CandidateInboxResponse>() {

			@Override
			public int compare(CandidateInboxResponse o1, CandidateInboxResponse o2) {

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
		PageImpl<CandidateInboxResponse> pageImplAllNotification = new PageImpl<CandidateInboxResponse>(candidateInboxResponses, pageable, candidateInboxResponses.size());
		CandidateInboxResponseWithPagination allWithPagination = new CandidateInboxResponseWithPagination();
		allWithPagination.setAllNotifications(pageImplAllNotification.getContent());
		allWithPagination.setLastPage(pageImplAllNotification.isLast());
		allWithPagination.setPageNumber(pageImplAllNotification.getNumber());
		allWithPagination.setPageSize(pageImplAllNotification.getSize());
		allWithPagination.setTotalElement(pageImplAllNotification.getTotalElements());
		allWithPagination.setTotalPages(pageImplAllNotification.getTotalPages());
		return allWithPagination;
	}

	@Override
	public List<CandidateInboxResponse> shortListCandidateNotifion(List<Long> notificationId) {
		List<CandidateInboxNotification> findAllById = this.candidateInboxNotificationRepository
				.findAllById(notificationId);
		findAllById.forEach(inbox -> {
			if (inbox.getStatus()
					.contains(ConstantConfiguration.NOTIFICATION_SHORTLISTED)) {

			} else {
				inbox.setStatus(ConstantConfiguration.NOTIFICATION_SHORTLISTED);
			}
		});
		List<CandidateInboxNotification> saveAll = this.candidateInboxNotificationRepository.saveAll(findAllById);
		List<CandidateInboxResponse> candidateInboxResponses = new ArrayList<CandidateInboxResponse>();
		saveAll.forEach(data -> {

			Job job = this.jobRepository.findById(data.getJobId())
					.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

			Company company = this.companyRepository.findById(data.getCompanyId())
					.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

			CandidateInboxResponse candidateInboxResponse = new CandidateInboxResponse();
			candidateInboxResponse.setId(data.getId());

			candidateInboxResponse.setCompanyName(company.getName());
			candidateInboxResponse.setJobName(job.getTitle());
			candidateInboxResponse.setLocation(job.getLocation());

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
					candidateInboxResponse.setActiveHour(diffInMinutes + " mins");
				} else if (diffInHours >= 1 && diffInHours < 24) {
					candidateInboxResponse.setActiveHour(diffInHours + " hour");
				} else {
					candidateInboxResponse.setActiveHour(diffInDays + " day");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			candidateInboxResponses.add(candidateInboxResponse);
		});

		Collections.sort(candidateInboxResponses, new Comparator<CandidateInboxResponse>() {

			@Override
			public int compare(CandidateInboxResponse o1, CandidateInboxResponse o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		return candidateInboxResponses;
	}

	@Override
	public CandidateInboxResponseWithPagination getShortListCandidateNotifion(long candidateId,Integer page, Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		List<CandidateInboxNotification> inboxList = this.candidateInboxNotificationRepository
				.findByCandidateId(candidateId);
		List<CandidateInboxResponse> candidateInboxResponses = new ArrayList<CandidateInboxResponse>();
		inboxList.forEach(data -> {
			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_SHORTLISTED)) {
				System.out.println(data.toString());
				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Company company = this.companyRepository.findById(data.getCompanyId())
						.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

				CandidateInboxResponse candidateInboxResponse = new CandidateInboxResponse();

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
						candidateInboxResponse.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						candidateInboxResponse.setActiveHour(diffInHours + " hour");
					} else {
						candidateInboxResponse.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				candidateInboxResponse.setCompanyName(company.getName());
				candidateInboxResponse.setJobName(job.getTitle());
				candidateInboxResponse.setLocation(job.getLocation());
				candidateInboxResponse.setId(data.getId());
				candidateInboxResponse.setCompanyLogo(fileGetUrl + company.getLogo());
				candidateInboxResponse.setExperience(job.getExperience());
				System.err.println(job.getDescription());
				candidateInboxResponse.setDescription(job.getDescription());
				candidateInboxResponse.setStatus(data.getStatus());
				candidateInboxResponse.setStatusSeen(data.getStatusSeen());
				candidateInboxResponses.add(candidateInboxResponse);
			}
		});

		Collections.sort(candidateInboxResponses, new Comparator<CandidateInboxResponse>() {

			@Override
			public int compare(CandidateInboxResponse o1, CandidateInboxResponse o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		
		
		PageImpl<CandidateInboxResponse> pageImplAllNotification = new PageImpl<CandidateInboxResponse>(candidateInboxResponses, pageable, candidateInboxResponses.size());
		CandidateInboxResponseWithPagination allWithPagination = new CandidateInboxResponseWithPagination();
		allWithPagination.setAllNotifications(pageImplAllNotification.getContent());
		allWithPagination.setLastPage(pageImplAllNotification.isLast());
		allWithPagination.setPageNumber(pageImplAllNotification.getNumber());
		allWithPagination.setPageSize(pageImplAllNotification.getSize());
		allWithPagination.setTotalElement(pageImplAllNotification.getTotalElements());
		allWithPagination.setTotalPages(pageImplAllNotification.getTotalPages());
		return allWithPagination;

	}

	@Override
	public List<CandidateInboxResponse> trashListCandidateNotifion(List<Long> notificationId) {

		List<CandidateInboxNotification> findAllById = this.candidateInboxNotificationRepository
				.findAllById(notificationId);
		findAllById.forEach(inbox -> {
			if (inbox.getStatus()
					.contains(ConstantConfiguration.NOTIFICATION_TRASH)) {

			} else {
				inbox.setStatus(ConstantConfiguration.NOTIFICATION_TRASH);
			}
		});
		List<CandidateInboxNotification> saveAll = this.candidateInboxNotificationRepository.saveAll(findAllById);
		List<CandidateInboxResponse> candidateInboxResponses = new ArrayList<CandidateInboxResponse>();
		saveAll.forEach(data -> {

			Job job = this.jobRepository.findById(data.getJobId())
					.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

			Company company = this.companyRepository.findById(data.getCompanyId())
					.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

			CandidateInboxResponse candidateInboxResponse = new CandidateInboxResponse();
			candidateInboxResponse.setId(data.getId());

			candidateInboxResponse.setCompanyName(company.getName());
			candidateInboxResponse.setJobName(job.getTitle());
			candidateInboxResponse.setLocation(job.getLocation());

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
					candidateInboxResponse.setActiveHour(diffInMinutes + " mins");
				} else if (diffInHours >= 1 && diffInHours < 24) {
					candidateInboxResponse.setActiveHour(diffInHours + " hour");
				} else {
					candidateInboxResponse.setActiveHour(diffInDays + " day");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			candidateInboxResponses.add(candidateInboxResponse);
		});

		Collections.sort(candidateInboxResponses, new Comparator<CandidateInboxResponse>() {

			@Override
			public int compare(CandidateInboxResponse o1, CandidateInboxResponse o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		return candidateInboxResponses;
	}

	@Override
	public CandidateInboxResponseWithPagination getTrashListCandidateNotifion(Long candidateId,Integer page, Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<CandidateInboxNotification> inboxList = this.candidateInboxNotificationRepository
				.findByCandidateId(candidateId);
		List<CandidateInboxResponse> candidateInboxResponses = new ArrayList<CandidateInboxResponse>();
		inboxList.forEach(data -> {

			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_TRASH)) {

				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Company company = this.companyRepository.findById(data.getCompanyId())
						.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

				CandidateInboxResponse candidateInboxResponse = new CandidateInboxResponse();

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
						candidateInboxResponse.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						candidateInboxResponse.setActiveHour(diffInHours + " hour");
					} else {
						candidateInboxResponse.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				candidateInboxResponse.setCompanyName(company.getName());
				candidateInboxResponse.setJobName(job.getTitle());
				candidateInboxResponse.setLocation(job.getLocation());
				candidateInboxResponse.setId(data.getId());
				candidateInboxResponse.setCompanyLogo(fileGetUrl + company.getLogo());
				candidateInboxResponse.setExperience(job.getExperience());
				System.err.println(job.getDescription());
				candidateInboxResponse.setDescription(job.getDescription());
				candidateInboxResponse.setStatus(data.getStatus());
				candidateInboxResponse.setStatusSeen(data.getStatusSeen());
				candidateInboxResponses.add(candidateInboxResponse);
			}
		});

		Collections.sort(candidateInboxResponses, new Comparator<CandidateInboxResponse>() {

			@Override
			public int compare(CandidateInboxResponse o1, CandidateInboxResponse o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		PageImpl<CandidateInboxResponse> pageImplAllNotification = new PageImpl<CandidateInboxResponse>(candidateInboxResponses, pageable, candidateInboxResponses.size());
		CandidateInboxResponseWithPagination allWithPagination = new CandidateInboxResponseWithPagination();
		allWithPagination.setAllNotifications(pageImplAllNotification.getContent());
		allWithPagination.setLastPage(pageImplAllNotification.isLast());
		allWithPagination.setPageNumber(pageImplAllNotification.getNumber());
		allWithPagination.setPageSize(pageImplAllNotification.getSize());
		allWithPagination.setTotalElement(pageImplAllNotification.getTotalElements());
		allWithPagination.setTotalPages(pageImplAllNotification.getTotalPages());
		return allWithPagination;
	}

	@Override
	public void changeStatusUnseenToSeen(Long notificationId) {
		CandidateInboxNotification notification = this.candidateInboxNotificationRepository.findById(notificationId)
				.orElseThrow(() -> new ResourceNotFoundException("Notification Not Found"));
		notification.setStatusSeen(ConstantConfiguration.NOTIFICATION_SEEN);
		this.candidateInboxNotificationRepository.save(notification);
	}

	@Override
	public CandidateInboxResponse getNotificationByCandidateIdAndCompanyIdAndJobId(Long candidateId, Long companyId,
			Long jobId) {
		CandidateInboxNotification data = this.candidateInboxNotificationRepository
				.findByCandidateIdAndCompanyIdAndJobId(candidateId, companyId, jobId)
				.orElseThrow(() -> new ResourceNotFoundException("Notification Not Found"));

		Job job = this.jobRepository.findById(data.getJobId())
				.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

		Company company = this.companyRepository.findById(data.getCompanyId())
				.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

		CandidateInboxResponse candidateInboxResponse = new CandidateInboxResponse();

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
				candidateInboxResponse.setActiveHour(diffInMinutes + " mins");
			} else if (diffInHours >= 1 && diffInHours < 24) {
				candidateInboxResponse.setActiveHour(diffInHours + " hour");
			} else {
				candidateInboxResponse.setActiveHour(diffInDays + " day");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		candidateInboxResponse.setCompanyName(company.getName());
		candidateInboxResponse.setJobName(job.getTitle());
		candidateInboxResponse.setLocation(job.getLocation());
		candidateInboxResponse.setId(data.getId());
		candidateInboxResponse.setCompanyLogo(fileGetUrl + company.getLogo());
		candidateInboxResponse.setExperience(job.getExperience());
		System.err.println(job.getDescription());
		candidateInboxResponse.setDescription(job.getDescription());
		candidateInboxResponse.setStatus(data.getStatus());
		candidateInboxResponse.setStatusSeen(data.getStatusSeen());

		return candidateInboxResponse;
	}

	@Override
	public void deleteNotificationByCandidateIdAndCompanyIdAndJobId(Long candidateId, Long companyId, Long jobId) {
		CandidateInboxNotification data = this.candidateInboxNotificationRepository
				.findByCandidateIdAndCompanyIdAndJobId(candidateId, companyId, jobId)
				.orElseThrow(() -> new ResourceNotFoundException("Notification Not Found"));

		System.err.println("delete notification by Id : " + data.toString());

		this.candidateInboxNotificationRepository.delete(data);

	}

	@Override
	public Map<String, String> sendNotificationToSearchedPeople(long candidateId, long companyId) {
		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);
		Candidate candidate = this.candidateRepository.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));
		Company company = this.companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

		CandidateInboxNotification candidateInboxNotification = new CandidateInboxNotification();
		candidateInboxNotification.setCandidateId(candidate.getId());
		candidateInboxNotification.setCompanyId(company.getId());
		candidateInboxNotification.setStatus(ConstantConfiguration.NOTIFICATION_RECIVED);
		candidateInboxNotification.setStatusSeen(ConstantConfiguration.NOTIFICATION_UNSEEN);
		candidateInboxNotification.setCreatedOn(utilDate);
		candidateInboxNotification.setModifiedOn(utilDate);
		this.candidateInboxNotificationRepository.save(candidateInboxNotification);
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", "LinkShaired SuccessFully");

		return map;
	}

	@Override
	public CandidateInboxResponseWithPagination getNotficationListwithPagination(Integer page, Integer size,
			Long candidateId) {

		Pageable pageable = PageRequest.of(page, size);
		List<CandidateInboxResponse> candidateInboxResponses = new ArrayList<CandidateInboxResponse>();
		List<CandidateInboxReponseShortlisted> candidateInboxReponseShortlisteds = new ArrayList<CandidateInboxReponseShortlisted>();
		List<CandidateInboxReponseRecieved> candidateInboxReponseReceived = new ArrayList<>();
		List<CandidateInboxNotification> findByCandidateId = this.candidateInboxNotificationRepository
				.findByCandidateId(candidateId);
		
		findByCandidateId.forEach(data -> {
			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_TRASH)) {
				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));
				Company company = this.companyRepository.findById(data.getCompanyId())
						.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));
				CandidateInboxResponse candidateInboxResponse = new CandidateInboxResponse();
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
						candidateInboxResponse.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						candidateInboxResponse.setActiveHour(diffInHours + " hour");
					} else {
						candidateInboxResponse.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				candidateInboxResponse.setCompanyName(company.getName());
				candidateInboxResponse.setJobName(job.getTitle());
				candidateInboxResponse.setLocation(job.getLocation());
				candidateInboxResponse.setId(data.getId());
				candidateInboxResponse.setCompanyLogo(fileGetUrl + company.getLogo());
				candidateInboxResponse.setExperience(job.getExperience());
				System.err.println(job.getDescription());
				candidateInboxResponse.setDescription(job.getDescription());
				candidateInboxResponse.setStatus(data.getStatus());
				candidateInboxResponse.setStatusSeen(data.getStatusSeen());
				candidateInboxResponses.add(candidateInboxResponse);
				
			}
			

			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_SHORTLISTED)) {
				System.out.println(data.toString());
				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Company company = this.companyRepository.findById(data.getCompanyId())
						.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

				CandidateInboxReponseShortlisted candidateInboxResponse = new CandidateInboxReponseShortlisted();

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
						candidateInboxResponse.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						candidateInboxResponse.setActiveHour(diffInHours + " hour");
					} else {
						candidateInboxResponse.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				candidateInboxResponse.setCompanyName(company.getName());
				candidateInboxResponse.setJobName(job.getTitle());
				candidateInboxResponse.setLocation(job.getLocation());
				candidateInboxResponse.setId(data.getId());
				candidateInboxResponse.setCompanyLogo(fileGetUrl + company.getLogo());
				candidateInboxResponse.setExperience(job.getExperience());
				candidateInboxResponse.setDescription(job.getDescription());
				candidateInboxResponse.setStatus(data.getStatus());
				candidateInboxResponse.setStatusSeen(data.getStatusSeen());
				candidateInboxReponseShortlisteds.add(candidateInboxResponse);
			}
			
			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_RECIVED)) {
				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));
				Company company = this.companyRepository.findById(data.getCompanyId())
						.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));
				CandidateInboxReponseRecieved candidateInboxResponse = new CandidateInboxReponseRecieved();
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
						candidateInboxResponse.setActiveHour(diffInMinutes + " mins");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						candidateInboxResponse.setActiveHour(diffInHours + " hour");
					} else {
						candidateInboxResponse.setActiveHour(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				candidateInboxResponse.setCompanyName(company.getName());
				candidateInboxResponse.setJobName(job.getTitle());
				candidateInboxResponse.setLocation(job.getLocation());
				candidateInboxResponse.setId(data.getId());
				candidateInboxResponse.setCompanyLogo(fileGetUrl + company.getLogo());
				candidateInboxResponse.setExperience(job.getExperience());
				System.err.println(job.getDescription());
				candidateInboxResponse.setDescription(job.getDescription());
				candidateInboxResponse.setStatus(data.getStatus());
				candidateInboxResponse.setStatusSeen(data.getStatusSeen());
				candidateInboxReponseReceived.add(candidateInboxResponse);

			}

		});

		Collections.sort(candidateInboxResponses, new Comparator<CandidateInboxResponse>() {

			@Override
			public int compare(CandidateInboxResponse o1, CandidateInboxResponse o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});
		
		Collections.sort(candidateInboxReponseShortlisteds, new Comparator<CandidateInboxReponseShortlisted>() {

			@Override
			public int compare(CandidateInboxReponseShortlisted o1, CandidateInboxReponseShortlisted o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});
		
		Collections.sort(candidateInboxReponseReceived, new Comparator<CandidateInboxReponseRecieved>() {

			@Override
			public int compare(CandidateInboxReponseRecieved o1, CandidateInboxReponseRecieved o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});
		
		PageImpl<CandidateInboxResponse> pageImpl = new PageImpl<CandidateInboxResponse>(candidateInboxResponses,
				pageable, 10L);
		CandidateInboxTrashNotification candidateInboxTrashNotification = new CandidateInboxTrashNotification();
		candidateInboxTrashNotification.setCandidateInboxResponsesTrash(pageImpl.getContent());
		candidateInboxTrashNotification.setLastPage(pageImpl.isLast());
		candidateInboxTrashNotification.setPageNumber(pageImpl.getNumber());
		candidateInboxTrashNotification.setPageSize(pageImpl.getSize());
		candidateInboxTrashNotification.setTotalElement(pageImpl.getTotalElements());
		candidateInboxTrashNotification.setTotalPages(pageImpl.getTotalPages());
		
		CandidateInpboxAllNotification candidateInpboxAllNotification = new CandidateInpboxAllNotification();
        PageImpl<CandidateInboxReponseRecieved> pageImplAll = new PageImpl<CandidateInboxReponseRecieved>(
		 candidateInboxReponseReceived, pageable, 10L);
		candidateInpboxAllNotification.setCandidateInboxReponseReceived(pageImplAll.getContent());
		candidateInpboxAllNotification.setLastPage(pageImplAll.isLast());
		candidateInpboxAllNotification.setPageNumber(pageImplAll.getNumber());
		candidateInpboxAllNotification.setPageSize(pageImplAll.getSize());
		candidateInpboxAllNotification.setTotalElement(pageImplAll.getTotalElements());
		candidateInpboxAllNotification.setTotalPages(pageImplAll.getTotalPages());
		
		CandidateInboxShortListNotification candidateInboxShortListNotification = new CandidateInboxShortListNotification();
		PageImpl<CandidateInboxReponseShortlisted> pageImpl2 = new PageImpl<CandidateInboxReponseShortlisted>(
				candidateInboxReponseShortlisteds, pageable, 10L);
		candidateInboxShortListNotification.setCandidateInboxReponseShortlisteds(pageImpl2.getContent());
		candidateInboxShortListNotification.setLastPage(pageImpl2.isLast());
		candidateInboxShortListNotification.setPageNumber(pageImpl2.getNumber());
		candidateInboxShortListNotification.setPageSize(pageImpl2.getSize());
		candidateInboxShortListNotification.setTotalElement(pageImpl2.getTotalElements());
		candidateInboxShortListNotification.setTotalPages(pageImpl2.getTotalPages());
		
		CandidateInboxResponseWithPagination candidateInboxResponseWithPagination = new CandidateInboxResponseWithPagination();
//		candidateInboxResponseWithPagination.setCandidateInboxShortListNotification(candidateInboxShortListNotification);
//		candidateInboxResponseWithPagination.setCandidateInboxTrashNotification(candidateInboxTrashNotification);
//		candidateInboxResponseWithPagination.setCandidateInpboxAllNotification(candidateInpboxAllNotification);
		return candidateInboxResponseWithPagination;
	}

	@Override
	public Map<String, String> sendNotificationToCandidateFromSearchedPeople(Long comanyId, Long candidateId,
			Long jobId) {
		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);

		Candidate candidate = this.candidateRepository.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

		Job job = this.jobRepository.findById(jobId).orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

		Company company = this.companyRepository.findById(comanyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company Not Found"));

		CandidateInboxNotification candidateInboxNotification = new CandidateInboxNotification();
		candidateInboxNotification.setCandidateId(candidate.getId());
		candidateInboxNotification.setCompanyId(company.getId());
		candidateInboxNotification.setJobId(job.getId());
		candidateInboxNotification.setStatus(ConstantConfiguration.NOTIFICATION_RECIVED);
		candidateInboxNotification.setStatusSeen(ConstantConfiguration.NOTIFICATION_UNSEEN);
		candidateInboxNotification.setCreatedOn(utilDate);
		candidateInboxNotification.setModifiedOn(utilDate);
		this.candidateInboxNotificationRepository.save(candidateInboxNotification);
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", "LinkShaired SuccessFully");
		return map;
	}

}
