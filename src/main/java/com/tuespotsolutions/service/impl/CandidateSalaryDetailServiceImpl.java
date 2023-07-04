package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.CandidateSalary;
import com.tuespotsolutions.models.CandidateSalaryDetail;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.CandidateSalaryRepository;
import com.tuespotsolutions.service.CandidateSalaryDetailService;

@Service
public class CandidateSalaryDetailServiceImpl implements CandidateSalaryDetailService {

	@Autowired
	public CandidateSalaryRepository candidateSalaryRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Override
	public CandidateSalaryDetail addSalaryDetail(CandidateSalaryDetail candidateSalaryDetail) {
		CandidateSalary candidateSalary = new CandidateSalary();

		Candidate candidate = this.candidateRepository.findById(candidateSalaryDetail.getCanidateId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Salary detail not found with id : " + candidateSalaryDetail.getCanidateId()));
		candidateSalary.setCandidate(candidate);
		candidateSalary.setCurrentCtc(candidateSalaryDetail.getCurrentCtc());
		candidateSalary.setExpectedCtc(candidateSalaryDetail.getExpectedCtc());
		candidateSalary.setNoticePeriod(candidateSalaryDetail.getNoticePeriod());

		CandidateSalary save = this.candidateSalaryRepository.save(candidateSalary);
		CandidateSalaryDetail candidateSalaryResponse = new CandidateSalaryDetail();
		candidateSalaryResponse.setId(save.getId());
		candidateSalaryResponse.setCurrentCtc(save.getCurrentCtc());
		candidateSalaryResponse.setExpectedCtc(save.getExpectedCtc());
		candidateSalaryResponse.setNoticePeriod(save.getNoticePeriod());

		return candidateSalaryResponse;
	}

	@Override
	public CandidateSalaryDetail updateSalaryDetail(CandidateSalaryDetail candidateSalaryDetail) {
		CandidateSalary candidateSalary = new CandidateSalary();

		Candidate candidate = this.candidateRepository.findById(candidateSalaryDetail.getCanidateId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate not found with id : " + candidateSalaryDetail.getCanidateId()));
		candidateSalary.setId(candidateSalaryDetail.getId());
		candidateSalary.setCandidate(candidate);
		candidateSalary.setCurrentCtc(candidateSalaryDetail.getCurrentCtc());
		candidateSalary.setExpectedCtc(candidateSalaryDetail.getExpectedCtc());
		candidateSalary.setNoticePeriod(candidateSalaryDetail.getNoticePeriod());

		CandidateSalary save = this.candidateSalaryRepository.save(candidateSalary);
		CandidateSalaryDetail candidateSalaryResponse = new CandidateSalaryDetail();
		candidateSalaryResponse.setId(save.getId());
		candidateSalaryResponse.setCurrentCtc(save.getCurrentCtc());
		candidateSalaryResponse.setExpectedCtc(save.getExpectedCtc());
		candidateSalaryResponse.setNoticePeriod(save.getNoticePeriod());
		return candidateSalaryResponse;
	}

	@Override
	public List<CandidateSalaryDetail> getSalaryDetailList(long canidateId) {
		Candidate candidate = this.candidateRepository.findById(canidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id : " + canidateId));
		List<CandidateSalary> findByCandidate = this.candidateSalaryRepository.findByCandidate(candidate);
		List<CandidateSalaryDetail> salaryDtailList = new ArrayList<CandidateSalaryDetail>();
		findByCandidate.forEach(salaryDtail -> {
			CandidateSalaryDetail candidateSalaryResponse = new CandidateSalaryDetail();
			candidateSalaryResponse.setId(salaryDtail.getId());
			candidateSalaryResponse.setCurrentCtc(salaryDtail.getCurrentCtc());
			candidateSalaryResponse.setExpectedCtc(salaryDtail.getExpectedCtc());
			candidateSalaryResponse.setNoticePeriod(salaryDtail.getNoticePeriod());
			salaryDtailList.add(candidateSalaryResponse);
		});
		return salaryDtailList;
	}

	@Override
	public CandidateSalaryDetail getSalaryDetailById(long salaryDetailId) {
		CandidateSalary candidateSalary = this.candidateSalaryRepository.findById(salaryDetailId).orElseThrow(
				() -> new ResourceNotFoundException("Salary Detail Not Found with id : " + salaryDetailId));
		CandidateSalaryDetail candidateSalaryResponse = new CandidateSalaryDetail();
		candidateSalaryResponse.setId(candidateSalary.getId());
		candidateSalaryResponse.setCurrentCtc(candidateSalary.getCurrentCtc());
		candidateSalaryResponse.setExpectedCtc(candidateSalary.getExpectedCtc());
		candidateSalaryResponse.setNoticePeriod(candidateSalary.getNoticePeriod());
		return candidateSalaryResponse;
	}

	@Override
	public void deleteSalaryDetail(long salaryDetailId) {
		CandidateSalary candidateSalary = this.candidateSalaryRepository.findById(salaryDetailId).orElseThrow(
				() -> new ResourceNotFoundException("Salary Detail Not Found with id : " + salaryDetailId));
		this.candidateSalaryRepository.delete(candidateSalary);
	}

}
