package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.DistrictResponse;

public interface DistrictService {

	//find district by stateId
	 List<DistrictResponse> findDistrictsByStateId(Integer stateId);
	
}
