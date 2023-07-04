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
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.City;
import com.tuespotsolutions.entity.CompanyInboxNotification;
import com.tuespotsolutions.entity.District;
import com.tuespotsolutions.entity.Job;
import com.tuespotsolutions.entity.ShortListCompanyInbox;
import com.tuespotsolutions.entity.State;
import com.tuespotsolutions.models.CompanyInboxNotificationAll;
import com.tuespotsolutions.models.ShortListCompanyInboxDetail;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.CityRepository;
import com.tuespotsolutions.repository.CompanyInboxNotificationRepository;
import com.tuespotsolutions.repository.CompanyRepository;
import com.tuespotsolutions.repository.DistrictRepository;
import com.tuespotsolutions.repository.JobAppliedRepository;
import com.tuespotsolutions.repository.JobRepository;
import com.tuespotsolutions.repository.ShortListCompanyInboxRepository;
import com.tuespotsolutions.repository.StateRepository;
import com.tuespotsolutions.service.ShortListCompanyInboxService;
import com.tuespotsolutions.util.ConstantConfiguration;

@Service
public class ShortListCompanyInboxServiceImpl implements ShortListCompanyInboxService {

	@Autowired
	private ShortListCompanyInboxRepository shortListCompanyInboxRepository;

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
	private StateRepository stateRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private CityRepository cityRepository;

	@Value("${file.upload.path}")
	private String fileUploadUrl;

	@Value("${file.get.url}")
	private String downloadResume;

	@Override
	public List<ShortListCompanyInboxDetail> shortListCompanyNotifications(List<Long> notificationId) {

		List<CompanyInboxNotification> inboxList = this.companyInboxNotificationRepository.findAllById(notificationId);
		List<ShortListCompanyInbox> shortListCompanyInbox = new ArrayList<ShortListCompanyInbox>();
		inboxList.forEach(inbox -> {
			inbox.setStatus(ConstantConfiguration.NOTIFICATION_SHORTLISTED);
			CompanyInboxNotification save = this.companyInboxNotificationRepository.save(inbox);
			if (save.getStatus().equalsIgnoreCase(ConstantConfiguration.NOTIFICATION_SHORTLISTED)) {
				ShortListCompanyInbox companyInbox = new ShortListCompanyInbox();
				companyInbox.setCandidateId(save.getCandidateId());
				companyInbox.setCompanyId(save.getCompanyId());
				companyInbox.setJobId(save.getJobId());
				java.util.Date utilDate = new java.util.Date();
				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				companyInbox.setModifiedOn(utilDate);
				companyInbox.setCreatedOn(utilDate);
				companyInbox.setStatus(ConstantConfiguration.NOTIFICATION_SHORTLISTED);
				companyInbox.setNotificationId(inbox.getId());
				shortListCompanyInbox.add(companyInbox);
			}
		});

		List<ShortListCompanyInbox> saveAll = this.shortListCompanyInboxRepository.saveAll(shortListCompanyInbox);
		List<ShortListCompanyInboxDetail> companyInboxDetailsResponse = new ArrayList<ShortListCompanyInboxDetail>();
		saveAll.forEach(inbox -> {
			ShortListCompanyInboxDetail companyInboxDetail = new ShortListCompanyInboxDetail();
//			companyInboxDetail.setCandidateId(inbox.getCandidateId());
//			companyInboxDetail.setCompanyId(inbox.getCompanyId());
//			companyInboxDetail.setJobId(inbox.getJobId());
//			companyInboxDetail.setModifiedOn(inbox.getModifiedOn());
//			companyInboxDetail.setCreatedOn(inbox.getCreatedOn());
//			companyInboxDetail.setStatus(inbox.getStatus());
//			companyInboxDetailsResponse.add(companyInboxDetail);
		});
		return companyInboxDetailsResponse;
	}

	@Override
	public List<ShortListCompanyInboxDetail> getShortListCompanyNotificationListByCompanyId(long companyId) {

		List<ShortListCompanyInbox> findByCompanyId = this.shortListCompanyInboxRepository.findByCompanyId(companyId);
		List<ShortListCompanyInboxDetail> companyInboxDetailsResponse = new ArrayList<ShortListCompanyInboxDetail>();
		findByCompanyId.forEach(data -> {
//			ShortListCompanyInboxDetail companyInboxNotificationResponse = new ShortListCompanyInboxDetail();
//			companyInboxDetail.setCandidateId(inbox.getCandidateId());
//			companyInboxDetail.setCompanyId(inbox.getCompanyId());
//			companyInboxDetail.setJobId(inbox.getJobId());
//			companyInboxDetail.setModifiedOn(inbox.getModifiedOn());
//			companyInboxDetail.setCreatedOn(inbox.getCreatedOn());
//			companyInboxDetail.setStatus(inbox.getStatus());
//			companyInboxDetailsResponse.add(companyInboxDetail);

			if (data.getStatus().contains(ConstantConfiguration.NOTIFICATION_SHORTLISTED)
					|| data.getStatus().contains(ConstantConfiguration.NOTIFICATION_SEEN)) {

				Job job = this.jobRepository.findById(data.getJobId())
						.orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));

				Candidate candidate = this.candidateRepository.findById(data.getCandidateId())
						.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Found"));

				// CompanyInboxNotificationResponse companyInboxNotificationResponse = new
				// CompanyInboxNotificationResponse();
				ShortListCompanyInboxDetail companyInboxNotificationResponse = new ShortListCompanyInboxDetail();
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
				companyInboxNotificationResponse.setNotifiationId(data.getNotificationId());
				companyInboxDetailsResponse.add(companyInboxNotificationResponse);
			}
		});

		Collections.sort(companyInboxDetailsResponse, new Comparator<ShortListCompanyInboxDetail>() {

			@Override
			public int compare(ShortListCompanyInboxDetail o1, ShortListCompanyInboxDetail o2) {

				if (o1.getId() < o2.getId()) {
					return 1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return -1;
				}

			}

		});

		return companyInboxDetailsResponse;

	}

	@Override
	public void setSeenStatusOnNotifiation(long notificationId) {
		ShortListCompanyInbox inbox = this.shortListCompanyInboxRepository.findById(notificationId)
				.orElseThrow(() -> new ResourceNotFoundException("ShortListed Notification Not Found"));
		inbox.setStatus(ConstantConfiguration.NOTIFICATION_SEEN);
		this.shortListCompanyInboxRepository.save(inbox);
	}

}
