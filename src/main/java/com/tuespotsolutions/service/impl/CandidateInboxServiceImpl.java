package com.tuespotsolutions.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.models.CandidateJobsNotificationsInbox;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.JobRepository;
import com.tuespotsolutions.repository.UserRepository;
import com.tuespotsolutions.service.CandidateInboxService;

@Service
public class CandidateInboxServiceImpl implements CandidateInboxService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private JobRepository jobRepository;

	@Override
	public List<CandidateJobsNotificationsInbox> candidateInboxJobNotifications(Long userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate Not exist with this id : " + userId));

		Candidate candiateProfile = this.candidateRepository.findById(user.getTypeId()).orElseThrow(
				() -> new ResourceNotFoundException("Candidate Profile Not exist with User Id : " + userId));
		
		
		return null;
	}

}
