package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.CandidateLanguage;

public interface CandidateLanguageService {
	
	public CandidateLanguage addLanguage(CandidateLanguage candidateLanguage);
	public CandidateLanguage updateLanguage(CandidateLanguage candidateLanguage);
	public List<CandidateLanguage> getLanguageListByCandidateId(long candidateId);
	public CandidateLanguage getLanguageByLanguageId(long languageId);
	public void deleteLanguageByLanguageId(long languageId);
}
