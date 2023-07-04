package com.tuespotsolutions.service;

import java.util.Map;

import com.tuespotsolutions.models.JobAppliedRequest;

public interface JobAppliedService {

	public Map<String, String> jobApply(JobAppliedRequest jobAppliedRequest);	
	
}
