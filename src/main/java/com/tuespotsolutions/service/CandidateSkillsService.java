package com.tuespotsolutions.service;

import java.util.List;
import java.util.Map;

import com.tuespotsolutions.models.CandidateSkillsRequest;

public interface CandidateSkillsService {

	public Map<String, String> addSillDetail(List<CandidateSkillsRequest> candidateSkillsRequest);
	public List<CandidateSkillsRequest> getSkillDetailList();
	public CandidateSkillsRequest getSkillDetailById(long skillId);
	public List<CandidateSkillsRequest> getSkillDetailByCandidateId(Long candidateId);
	public void deleteSalaryDetail(long salaryDetailId);
}
