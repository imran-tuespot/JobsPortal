package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.models.CandidateExperience;
import com.tuespotsolutions.repository.CandidateExperienceRepository;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.service.CandidateExperienceService;

@Service
public class CandidateExperienceServiceImpl implements CandidateExperienceService {

	@Autowired
	private CandidateExperienceRepository candidateExperienceRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Override
	public CandidateExperience addCanidateExperience(CandidateExperience candidateExperience) {
		com.tuespotsolutions.entity.CandidateExperience experience = new com.tuespotsolutions.entity.CandidateExperience();
		experience.setCompanyName(candidateExperience.getCompanyName());

		Candidate candidate = this.candidateRepository.findById(candidateExperience.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate not exist with id " + candidateExperience.getCandidateId()));

		experience.setCandidate(candidate);
		experience.setCtc(candidateExperience.getCtc());
		experience.setCurrent(true);
		experience.setDesignation(candidateExperience.getDesignation());
		experience.setEmploymentType(candidateExperience.getEmploymentType());
		experience.setJobProfile(candidateExperience.getJobProfile());
		experience.setJoingDate(candidateExperience.getJoingDate());
		experience.setEndDate(candidateExperience.getEndDate());
		
		com.tuespotsolutions.entity.CandidateExperience save = this.candidateExperienceRepository.save(experience);
		CandidateExperience candidateExperienceResponse = new CandidateExperience();
		candidateExperienceResponse.setCompanyName(save.getCompanyName());
		candidateExperienceResponse.setCtc(save.getCtc());
		candidateExperienceResponse.setCurrent(true);
		candidateExperienceResponse.setDesignation(save.getDesignation());
		candidateExperienceResponse.setEmploymentType(save.getEmploymentType());
		candidateExperienceResponse.setJobProfile(save.getJobProfile());
		candidateExperienceResponse.setJoingDate(save.getJoingDate());
		candidateExperienceResponse.setEndDate(save.getEndDate());
		return candidateExperienceResponse;
	}

	@Override
	public CandidateExperience updateCanidateExperience(CandidateExperience candidateExperience) {
		com.tuespotsolutions.entity.CandidateExperience experience = new com.tuespotsolutions.entity.CandidateExperience();
		experience.setId(candidateExperience.getId());
		experience.setCompanyName(candidateExperience.getCompanyName());

		Candidate candidate = this.candidateRepository.findById(candidateExperience.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate not exist with id " + candidateExperience.getCandidateId()));

		experience.setCandidate(candidate);
		experience.setCtc(candidateExperience.getCtc());
		experience.setCurrent(true);
		experience.setDesignation(candidateExperience.getDesignation());
		experience.setEmploymentType(candidateExperience.getEmploymentType());
		experience.setJobProfile(candidateExperience.getJobProfile());
		experience.setJoingDate(candidateExperience.getJoingDate());
		experience.setEndDate(candidateExperience.getEndDate());
		
		com.tuespotsolutions.entity.CandidateExperience save = this.candidateExperienceRepository.save(experience);
		CandidateExperience candidateExperienceResponse = new CandidateExperience();
		candidateExperienceResponse.setId(save.getId());
		candidateExperienceResponse.setCompanyName(save.getCompanyName());
		candidateExperienceResponse.setCtc(save.getCtc());
		candidateExperienceResponse.setCurrent(true);
		candidateExperienceResponse.setDesignation(save.getDesignation());
		candidateExperienceResponse.setEmploymentType(save.getEmploymentType());
		candidateExperienceResponse.setJobProfile(save.getJobProfile());
		candidateExperienceResponse.setJoingDate(save.getJoingDate());
		candidateExperienceResponse.setEndDate(save.getEndDate());
		return candidateExperienceResponse;
	}

	@Override
	public List<CandidateExperience> getCandidateExperience(long candidateId) {
		Candidate candidate = this.candidateRepository.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate not exist with id " +candidateId));
		List<com.tuespotsolutions.entity.CandidateExperience> findByCandidate = this.candidateExperienceRepository.findByCandidate(candidate);
		List<CandidateExperience> candidateExperienceList = new ArrayList<CandidateExperience>();
		findByCandidate.forEach(candateList->{
			CandidateExperience candidateExperienceResponse = new CandidateExperience();
			candidateExperienceResponse.setId(candateList.getId());
			candidateExperienceResponse.setCompanyName(candateList.getCompanyName());
			candidateExperienceResponse.setCtc(candateList.getCtc());
			candidateExperienceResponse.setCurrent(candateList.isCurrent());
			candidateExperienceResponse.setDesignation(candateList.getDesignation());
			candidateExperienceResponse.setEmploymentType(candateList.getEmploymentType());
			candidateExperienceResponse.setJobProfile(candateList.getJobProfile());
			candidateExperienceResponse.setJoingDate(candateList.getJoingDate());
			candidateExperienceResponse.setEndDate(candateList.getEndDate());
			candidateExperienceList.add(candidateExperienceResponse);
		});
		return candidateExperienceList;
	}

	@Override
	public void deleteCandidateExperience(long experienceId) {
		com.tuespotsolutions.entity.CandidateExperience experience = this.candidateExperienceRepository.findById(experienceId).orElseThrow(() -> new ResourceNotFoundException(
				"Candidate experience not exist with id " +experienceId));
		this.candidateExperienceRepository.delete(experience);

	}

	@Override
	public CandidateExperience getCandidateExperienceByExperienceId(long experienceId) {
		com.tuespotsolutions.entity.CandidateExperience experience = this.candidateExperienceRepository.findById(experienceId).orElseThrow(() -> new ResourceNotFoundException(
				"Candidate experience not exist with id " +experienceId));
		CandidateExperience candidateExperienceResponse = new CandidateExperience();
		candidateExperienceResponse.setId(experience.getId());
		candidateExperienceResponse.setCompanyName(experience.getCompanyName());
		candidateExperienceResponse.setCtc(experience.getCtc());
		candidateExperienceResponse.setCurrent(experience.isCurrent());
		candidateExperienceResponse.setDesignation(experience.getDesignation());
		candidateExperienceResponse.setEmploymentType(experience.getEmploymentType());
		candidateExperienceResponse.setJobProfile(experience.getJobProfile());
		candidateExperienceResponse.setJoingDate(experience.getJoingDate());
		candidateExperienceResponse.setEndDate(experience.getEndDate());
		return candidateExperienceResponse;
	}

}
