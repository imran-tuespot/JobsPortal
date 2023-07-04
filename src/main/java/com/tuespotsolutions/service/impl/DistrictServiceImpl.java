package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.District;
import com.tuespotsolutions.entity.State;
import com.tuespotsolutions.models.DistrictResponse;
import com.tuespotsolutions.repository.DistrictRepository;
import com.tuespotsolutions.repository.StateRepository;
import com.tuespotsolutions.service.DistrictService;

@Service
public class DistrictServiceImpl implements DistrictService{
	
	@Autowired
	private DistrictRepository districtRepository;
	
	@Autowired
	private StateRepository  stateRepository;

	@Override
	public List<DistrictResponse> findDistrictsByStateId(Integer stateId) {
		State state = this.stateRepository.findById(stateId).orElseThrow(()-> new ResourceNotFoundException("State Id "+stateId+" is not exist"));
		List<District> district = this.districtRepository.findByStateId(state.getStateId());
		List<DistrictResponse> districtResponses = new ArrayList<DistrictResponse>();
		district.forEach(dist->{
			DistrictResponse districtResponse = new DistrictResponse();
			districtResponse.setId(dist.getDistrictId());
			districtResponse.setDistrictName(dist.getDistrictTitle());
			districtResponses.add(districtResponse);
		});
		return districtResponses;
	}

}
