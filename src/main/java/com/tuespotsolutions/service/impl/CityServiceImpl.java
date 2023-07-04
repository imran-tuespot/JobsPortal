package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.City;
import com.tuespotsolutions.entity.District;
import com.tuespotsolutions.models.CityResponse;
import com.tuespotsolutions.repository.CityRepository;
import com.tuespotsolutions.repository.DistrictRepository;
import com.tuespotsolutions.service.CityService;

@Service
public class CityServiceImpl implements CityService {
	
	Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	DistrictRepository districtRepository;

	@Override
	public List<CityResponse> findCityByDistrictId(Integer districtId) {

		logger.info("line no 34 findCityByDistrictId() method");
		
		District district = this.districtRepository.findById(districtId)
				.orElseThrow(() -> new ResourceNotFoundException("District Id " + districtId + " is not exist"));
		List<City> cityList = this.cityRepository.findByDistrictId(district.getDistrictId());
		List<CityResponse> cityResponses = new ArrayList<CityResponse>();
		cityList.forEach(city->{
			CityResponse cityResponse = new CityResponse();
			cityResponse.setId(city.getId());
			cityResponse.setCityName(city.getName());
			cityResponses.add(cityResponse);
		});
		 
		return cityResponses;
	}

}
