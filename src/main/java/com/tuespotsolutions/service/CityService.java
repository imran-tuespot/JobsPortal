package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.CityResponse;

public interface CityService {
	
	List<CityResponse> findCityByDistrictId(Integer districtId);

}
