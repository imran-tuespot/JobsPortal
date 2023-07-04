package com.tuespotsolutions.service;

import java.util.List;
import java.util.Map;

import com.tuespotsolutions.models.CandiateListWithPagination;
import com.tuespotsolutions.models.CandidateRegistrationResponse;
import com.tuespotsolutions.models.CandidateRequest;
import com.tuespotsolutions.models.SearchPeopleDetail;
import com.tuespotsolutions.models.SearchedPeopleResponse;

public interface CandidateRegistrationService {
	
	//register Candidate
	public Map<String, String> registerCandidate(CandidateRequest candidateRequest);
	public Map<String, String> updateCandidate(CandidateRegistrationResponse candidateRequest);
	
	// getCandidateById
	public CandidateRegistrationResponse getCandidate(long candidateId);
	
	public CandiateListWithPagination getCandidateList(Integer pageNumber, Integer pageSize);
	
	public List<SearchedPeopleResponse> searchedPeople(String profileHeadline);
	
	public SearchPeopleDetail searchedpeopleDetailById(Long candidateId);

}
