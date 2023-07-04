package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.CandidateEducation;


public interface CandidateEducationService {
	
	public CandidateEducation addCandidateEducation(CandidateEducation candidateEducation);
	public CandidateEducation updateCandidateEducation(CandidateEducation candidateEducation);
	public CandidateEducation getListCandiateEducationById(long educationId);
	public List<CandidateEducation> getListCandiateEducationByCandidate(long candidateId);
	public void deleteCandidateEducation(long candidateEducationId);
	
}
