package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.State;
import com.tuespotsolutions.models.StateResponse;
import com.tuespotsolutions.repository.StateRepository;
import com.tuespotsolutions.service.StateService;

@Service
public class StateServiceImpl implements StateService {
	
	@Autowired
	private StateRepository stateRepository;

	@Override
	public List<StateResponse> findStates() {
		List<State> states = this.stateRepository.findAll();
		List<StateResponse> stateResponse = new ArrayList<StateResponse>();
		states.forEach(state->{
			StateResponse response = new StateResponse();
			response.setId(state.getStateId());
			response.setStateName(state.getStateTitle());
			stateResponse.add(response);
		});
		return stateResponse;
	}

	@Override
	public StateResponse findStateById(Integer stateId) {
		State state = this.stateRepository.findById(stateId).orElseThrow(()-> new ResourceNotFoundException("State not found with stateId : "+stateId));
		StateResponse response = new StateResponse();
		response.setId(state.getStateId());
		response.setStateName(state.getStateTitle());
		return response;
	}

}
