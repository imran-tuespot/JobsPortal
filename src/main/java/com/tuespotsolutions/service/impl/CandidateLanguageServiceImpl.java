package com.tuespotsolutions.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.CandidateLanguages;
import com.tuespotsolutions.models.CandidateLanguage;
import com.tuespotsolutions.repository.CandidateLanguagesRepository;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.service.CandidateLanguageService;

@Service
public class CandidateLanguageServiceImpl implements CandidateLanguageService {

	@Autowired
	private CandidateLanguagesRepository candidateLanguagesRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Override
	public CandidateLanguage addLanguage(CandidateLanguage candidateLanguage) {

		Candidate candidate = this.candidateRepository.findById(candidateLanguage.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate not found with id : " + candidateLanguage.getCandidateId()));
		CandidateLanguages candidateLanguages = new CandidateLanguages();
		candidateLanguages.setCandidate(candidate);
		candidateLanguages.setLevel(candidateLanguage.getLevel());
		candidateLanguages.setName(candidateLanguage.getName());
		candidateLanguages.setRead(candidateLanguage.isRead());
		candidateLanguages.setSpeak(candidateLanguage.isSpeak());
		candidateLanguages.setWrite(candidateLanguage.isWrite());
		
		CandidateLanguages save = this.candidateLanguagesRepository.save(candidateLanguages);
		CandidateLanguage candidateLanguageResponse = new CandidateLanguage();
		candidateLanguageResponse.setId(save.getId());
		candidateLanguageResponse.setLevel(save.getLevel());
		candidateLanguageResponse.setName(save.getName());
		candidateLanguageResponse.setRead(save.isRead());
		candidateLanguageResponse.setSpeak(save.isSpeak());
		candidateLanguageResponse.setWrite(save.isWrite());
		return candidateLanguageResponse;
	}

	@Override
	public CandidateLanguage updateLanguage(CandidateLanguage candidateLanguage) {
		
		Candidate candidate = this.candidateRepository.findById(candidateLanguage.getCandidateId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Candidate not found with id : " + candidateLanguage.getCandidateId()));
		CandidateLanguages candidateLanguages = new CandidateLanguages();
		candidateLanguages.setId(candidateLanguage.getId());
		candidateLanguages.setCandidate(candidate);
		candidateLanguages.setLevel(candidateLanguage.getLevel());
		candidateLanguages.setName(candidateLanguage.getName());
		candidateLanguages.setRead(candidateLanguage.isRead());
		candidateLanguages.setSpeak(candidateLanguage.isSpeak());
		candidateLanguages.setWrite(candidateLanguage.isWrite());
		
		CandidateLanguages save = this.candidateLanguagesRepository.save(candidateLanguages);
		
		CandidateLanguage candidateLanguageResponse = new CandidateLanguage();
		candidateLanguageResponse.setId(save.getId());
		candidateLanguageResponse.setLevel(save.getLevel());
		candidateLanguageResponse.setName(save.getName());
		candidateLanguageResponse.setRead(save.isRead());
		candidateLanguageResponse.setSpeak(save.isSpeak());
		candidateLanguageResponse.setWrite(save.isWrite());
		return candidateLanguageResponse;
	}

	@Override
	public List<CandidateLanguage> getLanguageListByCandidateId(long candidateId) {
		
		return null;
	}

	@Override
	public CandidateLanguage getLanguageByLanguageId(long languageId) {
		CandidateLanguages languages = this.candidateLanguagesRepository.findById(languageId).orElseThrow(() -> new ResourceNotFoundException(
				"Candidate Language detail not found with id : " +languageId));
		CandidateLanguage candidateLanguageResponse = new CandidateLanguage();
		candidateLanguageResponse.setId(languages.getId());
		candidateLanguageResponse.setLevel(languages.getLevel());
		candidateLanguageResponse.setName(languages.getName());
		candidateLanguageResponse.setRead(languages.isRead());
		candidateLanguageResponse.setSpeak(languages.isSpeak());
		candidateLanguageResponse.setWrite(languages.isWrite());
		return candidateLanguageResponse;
	}

	@Override
	public void deleteLanguageByLanguageId(long languageId) {
		CandidateLanguages languages = this.candidateLanguagesRepository.findById(languageId).orElseThrow(() -> new ResourceNotFoundException(
				"Candidate Language detail not found with id : " +languageId));
		this.candidateLanguagesRepository.delete(languages);
	}

}
