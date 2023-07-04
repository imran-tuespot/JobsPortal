package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.entity.JobWorkMode;
import com.tuespotsolutions.models.JobWorkModeModel;
import com.tuespotsolutions.repository.JobWorkModeRepository;
import com.tuespotsolutions.service.JobWorkModeService;

@Service
public class JobWorkModeServiceImpl implements JobWorkModeService {

	@Autowired
	private JobWorkModeRepository jobWorkModeRepository;
	
	@Override
	public List<JobWorkModeModel> getJobWorkModeList() {
		List<JobWorkMode> findAll = jobWorkModeRepository.findAll();
		List<JobWorkModeModel> jobWorkModeModels = new ArrayList<JobWorkModeModel>();
		findAll.forEach(data->{
			JobWorkModeModel jobWorkModeModel = new JobWorkModeModel();
			jobWorkModeModel.setId(data.getId());
			jobWorkModeModel.setTitle(data.getTitle());
			jobWorkModeModels.add(jobWorkModeModel);
		});
		return jobWorkModeModels;
	}

}
