package com.tuespotsolutions.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.CompanyInboxNotification;
import com.tuespotsolutions.entity.Job;
import com.tuespotsolutions.entity.JobApplied;
import com.tuespotsolutions.models.JobAppliedRequest;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.CompanyInboxNotificationRepository;
import com.tuespotsolutions.repository.CompanyRepository;
import com.tuespotsolutions.repository.JobAppliedRepository;
import com.tuespotsolutions.repository.JobRepository;
import com.tuespotsolutions.service.JobAppliedService;
import com.tuespotsolutions.util.ConstantConfiguration;

@Service
public class JobAppliedServiceImpl implements JobAppliedService {

	@Autowired
	private JobAppliedRepository jobAppliedRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private CompanyInboxNotificationRepository companyInboxNotificationRepository;

	@Override
	public Map<String, String> jobApply(JobAppliedRequest jobAppliedRequest) {
		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);
		JobApplied jobApplied = new JobApplied();
		Job job = this.jobRepository.findById(jobAppliedRequest.getJobId())
				.orElseThrow(() -> new ResourceNotFoundException("Job not found"));
		Company company = this.companyRepository.findById(job.getCompany().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Company not found"));
		jobApplied.setJob(job);
		Candidate candidate = this.candidateRepository.findById(jobAppliedRequest.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException("Job not found"));
		jobApplied.setCandidates(candidate);
		jobApplied.setCreatedOn(utilDate);
		jobApplied.setModifiedOn(utilDate);
		jobApplied.setStatus(true);
		jobApplied.setCompanyId(company.getId());
		JobApplied save = this.jobAppliedRepository.save(jobApplied);

		CompanyInboxNotification companyInboxNotification = new CompanyInboxNotification();
		companyInboxNotification.setCandidateId(save.getCandidates().getId());
		companyInboxNotification.setCreatedOn(utilDate);
		companyInboxNotification.setJobId(save.getJob().getId());
		companyInboxNotification.setModifiedOn(utilDate);
		companyInboxNotification.setStatus(ConstantConfiguration.NOTIFICATION_RECIVED);
		companyInboxNotification.setStatusSeen(ConstantConfiguration.NOTIFICATION_UNSEEN);
		companyInboxNotification.setCompanyId(save.getJob().getCompany().getId());
		this.companyInboxNotificationRepository.save(companyInboxNotification);

		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "Job Applied Successfully !!");
		return map;
	}

}
