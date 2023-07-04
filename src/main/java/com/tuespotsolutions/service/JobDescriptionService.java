package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.JobDescriptionRequest;
import com.tuespotsolutions.models.JobDescriptionResponse;

public interface JobDescriptionService {

	public List<JobDescriptionResponse> addDescription(List<JobDescriptionRequest> descriptionRequests);
	
	public List<JobDescriptionResponse> getDescriptionWithJobId(long jobId);
	
}
