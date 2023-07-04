package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.CandidateProjects;
import com.tuespotsolutions.models.CandidateProjectDetail;
import com.tuespotsolutions.repository.CandidateProjectsRepository;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.service.CandidateProjectDetailService;

@Service
public class CandidateProjectDetailServiceImpI implements CandidateProjectDetailService {

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private CandidateProjectsRepository candidateProjectsRepository;

	@Override
	public CandidateProjectDetail addCandidateProjectDetail(CandidateProjectDetail candidateProjectDetail) {
		CandidateProjects candidateProjects = new CandidateProjects();

		Candidate candidate = this.candidateRepository.findById(candidateProjectDetail.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate not exist with id " + candidateProjectDetail.getCandidateId()));

		candidateProjects.setCandidate(candidate);
		candidateProjects.setClient(candidateProjectDetail.getClient());
		candidateProjects.setDescription(candidateProjectDetail.getDescription());
		candidateProjects.setEndDate(candidateProjectDetail.getProjectEndDate());
		candidateProjects.setLocation(candidateProjectDetail.getLocation());
		candidateProjects.setNatureOfEmployement(candidateProjectDetail.getNatureOfEmployement());
		candidateProjects.setProjectStatus(candidateProjectDetail.isProjectStatus());
		candidateProjects.setRole(candidateProjectDetail.getRole());
		candidateProjects.setRoleDescription(candidateProjectDetail.getRoleDescription());
		candidateProjects.setSkillSet(candidateProjectDetail.getSkillSet());
		candidateProjects.setStartDate(candidateProjectDetail.getProjectStartDate());
		candidateProjects.setTeamSize(candidateProjectDetail.getTeamSize());
		candidateProjects.setTitle(candidateProjectDetail.getTitle());

		CandidateProjects save = this.candidateProjectsRepository.save(candidateProjects);
		CandidateProjectDetail projectDetailResponse = new CandidateProjectDetail();
		projectDetailResponse.setClient(save.getClient());
		projectDetailResponse.setDescription(save.getDescription());
		projectDetailResponse.setProjectEndDate(save.getEndDate());
		projectDetailResponse.setLocation(save.getLocation());
		projectDetailResponse.setNatureOfEmployement(save.getNatureOfEmployement());
		projectDetailResponse.setProjectStatus(save.isProjectStatus());
		projectDetailResponse.setRole(save.getRole());
		projectDetailResponse.setRoleDescription(save.getRoleDescription());
		projectDetailResponse.setSkillSet(save.getSkillSet());
		projectDetailResponse.setProjectStartDate(save.getStartDate());
		projectDetailResponse.setTeamSize(save.getTeamSize());
		projectDetailResponse.setTitle(save.getTitle());
		projectDetailResponse.setId(save.getId());
		return projectDetailResponse;
	}

	@Override
	public CandidateProjectDetail updateCandidateProjectDetail(CandidateProjectDetail candidateProjectDetail) {

		CandidateProjects projects = this.candidateProjectsRepository.findById(candidateProjectDetail.getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate project detail not exist with id " + candidateProjectDetail.getId()));

		Candidate candidate = this.candidateRepository.findById(candidateProjectDetail.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate not exist with id " + candidateProjectDetail.getCandidateId()));
		projects.setId(candidateProjectDetail.getId());
		projects.setCandidate(candidate);
		projects.setClient(candidateProjectDetail.getClient());
		projects.setDescription(candidateProjectDetail.getDescription());
		projects.setEndDate(candidateProjectDetail.getProjectEndDate());
		projects.setLocation(candidateProjectDetail.getLocation());
		projects.setNatureOfEmployement(candidateProjectDetail.getNatureOfEmployement());
		projects.setProjectStatus(candidateProjectDetail.isProjectStatus());
		projects.setRole(candidateProjectDetail.getRole());
		projects.setRoleDescription(candidateProjectDetail.getRoleDescription());
		projects.setSkillSet(candidateProjectDetail.getSkillSet());
		projects.setStartDate(candidateProjectDetail.getProjectStartDate());
		projects.setTeamSize(candidateProjectDetail.getTeamSize());
		projects.setTitle(candidateProjectDetail.getTitle());

		CandidateProjects save = this.candidateProjectsRepository.save(projects);
		CandidateProjectDetail projectDetailResponse = new CandidateProjectDetail();
		projectDetailResponse.setClient(save.getClient());
		projectDetailResponse.setDescription(save.getDescription());
		projectDetailResponse.setProjectEndDate(save.getEndDate());
		projectDetailResponse.setLocation(save.getLocation());
		projectDetailResponse.setNatureOfEmployement(save.getNatureOfEmployement());
		projectDetailResponse.setProjectStatus(save.isProjectStatus());
		projectDetailResponse.setRole(save.getRole());
		projectDetailResponse.setRoleDescription(save.getRoleDescription());
		projectDetailResponse.setSkillSet(save.getSkillSet());
		projectDetailResponse.setProjectStartDate(save.getStartDate());
		projectDetailResponse.setTeamSize(save.getTeamSize());
		projectDetailResponse.setTitle(save.getTitle());
		projectDetailResponse.setId(save.getId());
		return projectDetailResponse;
	}

	@Override
	public List<CandidateProjectDetail> getCandidateProjectDetailList(Long candidateId) {
		Candidate candidate = this.candidateRepository.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate not exist with id " +candidateId));
		
		List<CandidateProjects> findByCandidate = this.candidateProjectsRepository.findByCandidate(candidate);
		List<CandidateProjectDetail> projectDetailResponseList = new ArrayList<CandidateProjectDetail>();
		findByCandidate.forEach(projectDetail->{
			CandidateProjectDetail projectDetailResponse = new CandidateProjectDetail();
			projectDetailResponse.setClient(projectDetail.getClient());
			projectDetailResponse.setDescription(projectDetail.getDescription());
			projectDetailResponse.setProjectEndDate(projectDetail.getEndDate());
			projectDetailResponse.setLocation(projectDetail.getLocation());
			projectDetailResponse.setNatureOfEmployement(projectDetail.getNatureOfEmployement());
			projectDetailResponse.setProjectStatus(projectDetail.isProjectStatus());
			projectDetailResponse.setRole(projectDetail.getRole());
			projectDetailResponse.setRoleDescription(projectDetail.getRoleDescription());
			projectDetailResponse.setSkillSet(projectDetail.getSkillSet());
			projectDetailResponse.setProjectStartDate(projectDetail.getStartDate());
			projectDetailResponse.setTeamSize(projectDetail.getTeamSize());
			projectDetailResponse.setTitle(projectDetail.getTitle());
			projectDetailResponse.setId(projectDetail.getId());
			projectDetailResponseList.add(projectDetailResponse);
		});
		return projectDetailResponseList;
	}

	@Override
	public CandidateProjectDetail getCandidateProjectDetailById(Long projectDetailId) {
		CandidateProjects projects = this.candidateProjectsRepository.findById(projectDetailId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate project detail not exist with id " +projectDetailId));
		
		CandidateProjectDetail projectDetailResponse = new CandidateProjectDetail();
		projectDetailResponse.setClient(projects.getClient());
		projectDetailResponse.setDescription(projects.getDescription());
		projectDetailResponse.setProjectEndDate(projects.getEndDate());
		projectDetailResponse.setLocation(projects.getLocation());
		projectDetailResponse.setNatureOfEmployement(projects.getNatureOfEmployement());
		projectDetailResponse.setProjectStatus(projects.isProjectStatus());
		projectDetailResponse.setRole(projects.getRole());
		projectDetailResponse.setRoleDescription(projects.getRoleDescription());
		projectDetailResponse.setSkillSet(projects.getSkillSet());
		projectDetailResponse.setProjectStartDate(projects.getStartDate());
		projectDetailResponse.setTeamSize(projects.getTeamSize());
		projectDetailResponse.setTitle(projects.getTitle());
		projectDetailResponse.setId(projects.getId());
		return projectDetailResponse;
	}

	@Override
	public void deleteCandidateProjectDetail(Long projectDetailId) {
		CandidateProjects projects = this.candidateProjectsRepository.findById(projectDetailId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate project detail not exist with id " +projectDetailId));
		this.candidateProjectsRepository.delete(projects);
	}

}
