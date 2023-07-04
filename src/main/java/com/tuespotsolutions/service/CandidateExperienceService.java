package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.CandidateExperience;

public interface CandidateExperienceService {

	public CandidateExperience addCanidateExperience(CandidateExperience candidateExperience);
	
	public CandidateExperience updateCanidateExperience(CandidateExperience candidateExperience);
	
	public List<CandidateExperience> getCandidateExperience(long candidateId);
	
	public CandidateExperience getCandidateExperienceByExperienceId(long experienceId);
	
	public void deleteCandidateExperience(long experienceId);
	
}
