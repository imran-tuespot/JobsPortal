package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.CandidateProjectDetail;

public interface CandidateProjectDetailService {
	
	public CandidateProjectDetail  addCandidateProjectDetail(CandidateProjectDetail candidateProjectDetail);
	public CandidateProjectDetail  updateCandidateProjectDetail(CandidateProjectDetail candidateProjectDetail);
	public List<CandidateProjectDetail>  getCandidateProjectDetailList(Long candidateId);
	public CandidateProjectDetail  getCandidateProjectDetailById(Long projectDetailId);
	public void  deleteCandidateProjectDetail(Long projectDetailId);
}
