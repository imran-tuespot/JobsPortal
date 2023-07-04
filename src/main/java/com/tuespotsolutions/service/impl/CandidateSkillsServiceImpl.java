package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.CandidateSkills;
import com.tuespotsolutions.models.CandidateSkillsRequest;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.CandidateSkillsRepository;
import com.tuespotsolutions.service.CandidateSkillsService;

@Service
public class CandidateSkillsServiceImpl implements CandidateSkillsService {

	@Autowired
	private CandidateSkillsRepository candidateSkillsRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Override
	public Map<String, String> addSillDetail(List<CandidateSkillsRequest> candidateSkillsRequest) {
		List<CandidateSkills> candidateSkillsList = new ArrayList<CandidateSkills>();
		candidateSkillsRequest.forEach(data -> {
			CandidateSkills candidateSkills = new CandidateSkills();
			candidateSkills.setId(data.getId());
			candidateSkills.setSkill(data.getSkill());
			Candidate candidate = this.candidateRepository.findById(data.getCandidateId())
					.orElseThrow(() -> new ResourceNotFoundException("Canidate Not Exist !!"));
			candidateSkills.setCandidate(candidate);
			candidateSkillsList.add(candidateSkills);
		});

		this.candidateSkillsRepository.saveAll(candidateSkillsList);

		Map<String, String> respose = new HashMap<String, String>();
		respose.put("status", "Skill added successfully");
		return respose;
	}

	@Override
	public List<CandidateSkillsRequest> getSkillDetailList() {
		List<CandidateSkills> findAll = this.candidateSkillsRepository.findAll();
		List<CandidateSkillsRequest> response = new ArrayList<CandidateSkillsRequest>();
		findAll.forEach(data -> {
			CandidateSkillsRequest candidateSkillsRequest = new CandidateSkillsRequest();
			candidateSkillsRequest.setId(data.getId());
			candidateSkillsRequest.setSkill(data.getSkill());
			candidateSkillsRequest.setCandidateId(data.getCandidate().getId());
			response.add(candidateSkillsRequest);
		});
		return response;
	}

	@Override
	public CandidateSkillsRequest getSkillDetailById(long skillId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSalaryDetail(long salaryDetailId) {
		CandidateSkills skills = this.candidateSkillsRepository.findById(salaryDetailId)
				.orElseThrow(() -> new ResourceNotFoundException("Skill Not Exist !!"));
		this.candidateSkillsRepository.delete(skills);
	}

	@Override
	public List<CandidateSkillsRequest> getSkillDetailByCandidateId(Long candidateId) {
		Candidate candidate = this.candidateRepository.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Canidate Not Exist !!"));
		List<CandidateSkills> findByCandidate = this.candidateSkillsRepository.findByCandidate(candidate);
		
		List<CandidateSkillsRequest> candidateSkillsRequests = new ArrayList<CandidateSkillsRequest>();
		findByCandidate.forEach(data->{
			CandidateSkillsRequest candidateSkillsRequest = new CandidateSkillsRequest();
			candidateSkillsRequest.setId(data.getId());
			candidateSkillsRequest.setSkill(data.getSkill());
			candidateSkillsRequest.setCandidateId(data.getCandidate().getId());
			candidateSkillsRequests.add(candidateSkillsRequest);
		});
		return candidateSkillsRequests;
	}

}
