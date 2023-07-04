package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.CandidateInboxDetailResponse;
import com.tuespotsolutions.models.CandidateJobsNotificationsInbox;


public interface CandidateInboxService {
	
	public List<CandidateJobsNotificationsInbox>  candidateInboxJobNotifications(Long userId);
	
	
}
