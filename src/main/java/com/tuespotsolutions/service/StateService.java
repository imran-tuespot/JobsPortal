package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.StateResponse;

public interface StateService {
	
	// find all states
	List<StateResponse> findStates();
	
	// find by id
	StateResponse findStateById(Integer stateId);

}
