package com.tuespotsolutions.service;

import java.util.List;
import java.util.Map;

import com.tuespotsolutions.models.CandidateInboxDetailResponse;
import com.tuespotsolutions.models.CandidateInboxResponse;
import com.tuespotsolutions.models.CandidateInboxResponseWithPagination;
import com.tuespotsolutions.models.CompanyInboxNotificationAll;

public interface CandidateInboxNotificationService {
	
	public Map<String, String> sendNotificationToCandidate(Long notificationId);
	public List<CandidateInboxResponseWithPagination> getNotficationList(Integer page, Integer size,Long candidateId);
	public CandidateInboxDetailResponse getCandidateInboxNotificationDetail(Long notificationId);
	public CandidateInboxResponseWithPagination getLatestNotification(Long candidateId,Integer page, Integer size);
	
	public CandidateInboxResponse getNotificationByCandidateIdAndCompanyIdAndJobId(Long candidateId, Long companyId, Long JobId);
	
	public void changeStatusUnseenToSeen(Long notificationId);

	public List<CandidateInboxResponse> shortListCandidateNotifion(List<Long> notificationId); 
	public CandidateInboxResponseWithPagination getShortListCandidateNotifion(long candidateId,Integer page, Integer size);
	
	public List<CandidateInboxResponse> trashListCandidateNotifion(List<Long> notificationId); 
	public CandidateInboxResponseWithPagination getTrashListCandidateNotifion(Long candidateId,Integer page, Integer size); 
	
	public void deleteNotificationByCandidateIdAndCompanyIdAndJobId(Long candidateId, Long companyId, Long jobId);
	
	public Map<String, String> sendNotificationToSearchedPeople(long candidateId, long companyId);

	public CandidateInboxResponseWithPagination getNotficationListwithPagination(Integer page, Integer size,Long candidateId);
	
	public Map<String, String> sendNotificationToCandidateFromSearchedPeople(Long comanyId,Long candidateId, Long jobId);
}
