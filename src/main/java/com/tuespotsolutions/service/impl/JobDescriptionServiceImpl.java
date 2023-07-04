package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Job;
import com.tuespotsolutions.entity.JobDescription;
import com.tuespotsolutions.models.JobDescriptionRequest;
import com.tuespotsolutions.models.JobDescriptionResponse;
import com.tuespotsolutions.repository.JobDescriptionRespository;
import com.tuespotsolutions.repository.JobRepository;
import com.tuespotsolutions.service.JobDescriptionService;

@Service
public class JobDescriptionServiceImpl implements JobDescriptionService {

	@Autowired
	private JobDescriptionRespository descriptionRespository;

	@Autowired
	private JobRepository jobRepository;

	@Override
	public List<JobDescriptionResponse> addDescription(List<JobDescriptionRequest> descriptionRequests) {

		List<JobDescription> jobs = new ArrayList<JobDescription>();
		descriptionRequests.forEach(data -> {
			JobDescription description = new JobDescription();
			description.setDescription(data.getDescription());
			Job job = this.jobRepository.findById(data.getJobId())
					.orElseThrow(() -> new ResourceNotFoundException("Job not exist with Jobid : " + data.getJobId()));
			description.setJob(job);
			jobs.add(description);
		});
		List<JobDescription> saveAll = this.descriptionRespository.saveAll(jobs);
		List<JobDescriptionResponse> descriptionResponses = new ArrayList<JobDescriptionResponse>();
		saveAll.forEach(data->{
			JobDescriptionResponse descriptionResponse = new JobDescriptionResponse();
			descriptionResponse.setDescription(data.getDescription());
			descriptionResponse.setId(data.getId());
			descriptionResponses.add(descriptionResponse);
		});
		return descriptionResponses;
	}

	@Override
	public List<JobDescriptionResponse> getDescriptionWithJobId(long jobId) {
		Job job = this.jobRepository.findById(jobId)
				.orElseThrow(() -> new ResourceNotFoundException("Job not exist with Jobid : " + jobId));
		List<JobDescription> findByJob = this.descriptionRespository.findByJob(job);
		List<JobDescriptionResponse> descriptionResponses = new ArrayList<JobDescriptionResponse>();
		findByJob.forEach(data->{
			JobDescriptionResponse descriptionResponse = new JobDescriptionResponse();
			descriptionResponse.setDescription(data.getDescription());
			descriptionResponse.setId(data.getId());
			descriptionResponses.add(descriptionResponse);
		});
		return descriptionResponses;
	}

}
