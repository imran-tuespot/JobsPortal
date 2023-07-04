package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.models.CandidateEducation;
import com.tuespotsolutions.repository.CandidateEducationRepository;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.service.CandidateEducationService;

@Service
public class CandidateEducationServiceImpl implements CandidateEducationService {

	@Autowired
	private CandidateEducationRepository candidateEducationRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Override
	public CandidateEducation addCandidateEducation(CandidateEducation candidateEducation) {

		com.tuespotsolutions.entity.CandidateEducation candidate = new com.tuespotsolutions.entity.CandidateEducation();
		candidate.setName(candidateEducation.getName());
		candidate.setCourse(candidateEducation.getCourse());
		candidate.setCourseDuration(candidateEducation.getCourseDuration());
		candidate.setCourseType(candidateEducation.getCourseType());
		candidate.setGradingSystem(candidateEducation.getGradingSystem());
		candidate.setMarks(candidateEducation.getMarks());
		candidate.setSpecialization(candidateEducation.getSpecialization());
		candidate.setUniversity(candidateEducation.getUniversity());

		Candidate candidate2 = this.candidateRepository.findById(candidateEducation.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate not exist with Id : " + candidateEducation.getCandidateId()));
		candidate.setCandidate(candidate2);
		com.tuespotsolutions.entity.CandidateEducation save = this.candidateEducationRepository.save(candidate);
		CandidateEducation candidateEdu = new CandidateEducation();
		candidateEdu.setId(save.getId());
		candidateEdu.setName(save.getName());
		candidateEdu.setCourse(save.getCourse());
		candidateEdu.setCourseDuration(save.getCourseDuration());
		candidateEdu.setCourseType(save.getCourseType());
		candidateEdu.setGradingSystem(save.getGradingSystem());
		candidateEdu.setMarks(save.getMarks());
		candidateEdu.setSpecialization(save.getSpecialization());
		candidateEdu.setUniversity(save.getUniversity());
		return candidateEdu;
	}

	@Override
	public CandidateEducation updateCandidateEducation(CandidateEducation candidateEducation) {
		com.tuespotsolutions.entity.CandidateEducation candidate = new com.tuespotsolutions.entity.CandidateEducation();
		candidate.setId(candidateEducation.getId());
		candidate.setName(candidateEducation.getName());
		candidate.setCourse(candidateEducation.getCourse());
		candidate.setCourseDuration(candidateEducation.getCourseDuration());
		candidate.setCourseType(candidateEducation.getCourseType());
		candidate.setGradingSystem(candidateEducation.getGradingSystem());
		candidate.setMarks(candidateEducation.getMarks());
		candidate.setSpecialization(candidateEducation.getSpecialization());
		candidate.setUniversity(candidateEducation.getUniversity());

		Candidate candidate2 = this.candidateRepository.findById(candidateEducation.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate not exist with Id : " + candidateEducation.getCandidateId()));
		candidate.setCandidate(candidate2);
		com.tuespotsolutions.entity.CandidateEducation save = this.candidateEducationRepository.save(candidate);
		CandidateEducation candidateEdu = new CandidateEducation();
		candidateEdu.setId(save.getId());
		candidateEdu.setName(save.getName());
		candidateEdu.setCourse(save.getCourse());
		candidateEdu.setCourseDuration(save.getCourseDuration());
		candidateEdu.setCourseType(save.getCourseType());
		candidateEdu.setGradingSystem(save.getGradingSystem());
		candidateEdu.setMarks(save.getMarks());
		candidateEdu.setSpecialization(save.getSpecialization());
		candidateEdu.setUniversity(save.getUniversity());
		return candidateEdu;
	}

	@Override
	public List<CandidateEducation> getListCandiateEducationByCandidate(long candidateId) {

		Candidate candidate = this.candidateRepository.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not exist with Id : " + candidateId));

		List<com.tuespotsolutions.entity.CandidateEducation> findAll = this.candidateEducationRepository
				.findByCandidate(candidate);
		List<CandidateEducation> candidateEducations = new ArrayList<CandidateEducation>();
		findAll.forEach(data -> {
			CandidateEducation candidateEdu = new CandidateEducation();
			candidateEdu.setId(data.getId());
			candidateEdu.setName(data.getName());
			candidateEdu.setCourse(data.getCourse());
			candidateEdu.setCourseDuration(data.getCourseDuration());
			candidateEdu.setCourseType(data.getCourseType());
			candidateEdu.setGradingSystem(data.getGradingSystem());
			candidateEdu.setMarks(data.getMarks());
			candidateEdu.setSpecialization(data.getSpecialization());
			candidateEdu.setUniversity(data.getUniversity());
			candidateEducations.add(candidateEdu);
		});
		return candidateEducations;
	}

	@Override
	public void deleteCandidateEducation(long candidateEducationId) {
		com.tuespotsolutions.entity.CandidateEducation education = this.candidateEducationRepository
				.findById(candidateEducationId).orElseThrow(() -> new ResourceNotFoundException(
						"Candidate Education not exist with Id : " + candidateEducationId));
		this.candidateEducationRepository.delete(education);
	}

	@Override
	public CandidateEducation getListCandiateEducationById(long educationId) {
		com.tuespotsolutions.entity.CandidateEducation education = this.candidateEducationRepository
				.findById(educationId).orElseThrow(
						() -> new ResourceNotFoundException("Candidate education not exist with Id : " + educationId));
		CandidateEducation candidateEdu = new CandidateEducation();
		candidateEdu.setId(education.getId());
		candidateEdu.setName(education.getName());
		candidateEdu.setCourse(education.getCourse());
		candidateEdu.setCourseDuration(education.getCourseDuration());
		candidateEdu.setCourseType(education.getCourseType());
		candidateEdu.setGradingSystem(education.getGradingSystem());
		candidateEdu.setMarks(education.getMarks());
		candidateEdu.setSpecialization(education.getSpecialization());
		candidateEdu.setUniversity(education.getUniversity());
		return candidateEdu;
	}

}
