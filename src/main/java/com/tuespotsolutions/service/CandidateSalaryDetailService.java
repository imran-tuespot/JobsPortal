package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.CandidateSalaryDetail;

public interface CandidateSalaryDetailService {
	
	public CandidateSalaryDetail addSalaryDetail(CandidateSalaryDetail candidateSalaryDetail);
	public CandidateSalaryDetail updateSalaryDetail(CandidateSalaryDetail candidateSalaryDetail);
	public List<CandidateSalaryDetail> getSalaryDetailList(long canidateId);
	public CandidateSalaryDetail getSalaryDetailById(long salaryDetailId);
	public void deleteSalaryDetail(long salaryDetailId);

}
