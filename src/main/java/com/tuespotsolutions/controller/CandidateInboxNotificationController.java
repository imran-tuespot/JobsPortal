package com.tuespotsolutions.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.CandidateInboxDetailResponse;
import com.tuespotsolutions.models.CandidateInboxResponse;
import com.tuespotsolutions.models.CandidateInboxResponseWithPagination;
import com.tuespotsolutions.service.CandidateInboxNotificationService;

@RestController
@CrossOrigin("*")
@RequestMapping("/candidteinbox")
public class CandidateInboxNotificationController {

	@Autowired
	CandidateInboxNotificationService candidateInboxNotificationService;

	@PostMapping("/send")
	public ResponseEntity<?> shareInviteLink(@RequestParam("notificationId") Long notificationId) {
		Map<String, String> sendNotificationToCandidate = this.candidateInboxNotificationService
				.sendNotificationToCandidate(notificationId);
		return new ResponseEntity<Map<String, String>>(sendNotificationToCandidate, HttpStatus.OK);
	}
	@PostMapping("/sendInvite")
	public ResponseEntity<?> shareInviteLinktoCandidate(@RequestParam("companyId") Long comapnyId,
			@RequestParam("candidateId") Long candidateId,@RequestParam("jobId") Long jobId) {
		Map<String, String> sendNotificationToCandidate = this.candidateInboxNotificationService
				.sendNotificationToCandidateFromSearchedPeople(comapnyId, candidateId, jobId);
		return new ResponseEntity<Map<String, String>>(sendNotificationToCandidate, HttpStatus.OK);
	}

	@GetMapping("/shortlisted/by")
	public ResponseEntity<?> getShortListedCompanyInboxNotification(@RequestParam("candidateId") long candidateId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		CandidateInboxResponseWithPagination trashListCandidateNotifion = this.candidateInboxNotificationService
				.getShortListCandidateNotifion(candidateId,pageNumber,pageSize);
		return new ResponseEntity<CandidateInboxResponseWithPagination>(trashListCandidateNotifion, HttpStatus.OK);
	}

	@GetMapping("/trashed/by")
	public ResponseEntity<?> getTrashedCompanyInboxNotification(@RequestParam("companyId") long companyId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		CandidateInboxResponseWithPagination trashListCandidateNotifion = this.candidateInboxNotificationService
				.getTrashListCandidateNotifion(companyId,pageNumber,pageSize);
		return new ResponseEntity<CandidateInboxResponseWithPagination>(trashListCandidateNotifion, HttpStatus.OK);
	}

	@GetMapping("/get/list")
	public ResponseEntity<?> getNotificationList(@RequestParam("notificationId") Long notificationId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		List<CandidateInboxResponseWithPagination> notficationList = this.candidateInboxNotificationService
				.getNotficationList(pageNumber, pageSize, notificationId);
		return new ResponseEntity<List<CandidateInboxResponseWithPagination>>(notficationList, HttpStatus.OK);
	}

	@GetMapping("/notification/detail")
	public ResponseEntity<?> getCandidateNotificationDetail(@RequestParam("notificationId") Long notificationId) {
		CandidateInboxDetailResponse candidateInboxNotificationDetail = this.candidateInboxNotificationService
				.getCandidateInboxNotificationDetail(notificationId);
		return new ResponseEntity<CandidateInboxDetailResponse>(candidateInboxNotificationDetail, HttpStatus.OK);
	}

	@GetMapping("/latest/by")
	public ResponseEntity<?> getCandidateInboxNotificationLatest(@RequestParam("candidateId") Long candidateId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize	) {
		CandidateInboxResponseWithPagination latestNotification = this.candidateInboxNotificationService
				.getLatestNotification(candidateId,pageNumber,pageSize);
		return new ResponseEntity<CandidateInboxResponseWithPagination>(latestNotification, HttpStatus.OK);
	}

	@PostMapping("/changestatus")
	public ResponseEntity<?> NotificationSeenStatus(@RequestBody long notificationId) {
		this.candidateInboxNotificationService.changeStatusUnseenToSeen(notificationId);
		@SuppressWarnings("unchecked")
		Map<String, String> status = new HashedMap();
		status.put("status", "Notification seen");
		return new ResponseEntity<Map<String, String>>(status, HttpStatus.OK);
	}

	@PostMapping("/shortlist")
	public ResponseEntity<?> shortListedInboxNotification(@RequestBody List<Long> notificationId) {
		List<CandidateInboxResponse> shortListCandidateNotifion = this.candidateInboxNotificationService
				.shortListCandidateNotifion(notificationId);
		return new ResponseEntity<List<CandidateInboxResponse>>(shortListCandidateNotifion, HttpStatus.OK);

	}

	@PostMapping("/trashlist")
	public ResponseEntity<?> trashInboxNotification(@RequestBody List<Long> notificationId) {
		List<CandidateInboxResponse> trashListCandidateNotifion = this.candidateInboxNotificationService
				.trashListCandidateNotifion(notificationId);
		return new ResponseEntity<List<CandidateInboxResponse>>(trashListCandidateNotifion, HttpStatus.OK);

	}

	@GetMapping("/get/notification/by")
	public ResponseEntity<?> getNotificationByCandidateIdAndCompanyId(@RequestParam("candidateId") Long candidateId,
			@RequestParam("companyId") Long companyId,
			@RequestParam("jobId") Long jobId) {
		CandidateInboxResponse notificationByCandidateIdAndCompanyId = this.candidateInboxNotificationService
				.getNotificationByCandidateIdAndCompanyIdAndJobId(candidateId, companyId, jobId);
		return new ResponseEntity<CandidateInboxResponse>(notificationByCandidateIdAndCompanyId, HttpStatus.OK);
	}

	@DeleteMapping("/notification/delete/by")
	public ResponseEntity<?> deleteNotitfation(@RequestParam("candidateId") Long candidateId,
			@RequestParam("companyId") Long companyId,
			@RequestParam("jobId") Long jobId) {
		this.candidateInboxNotificationService.deleteNotificationByCandidateIdAndCompanyIdAndJobId(candidateId, companyId,jobId);
		Map<String, String> resp = new HashMap<String, String>();
		resp.put("status", "Invite Canceleted");
		return new ResponseEntity<Map<String, String>>(resp, HttpStatus.OK);
	}

	@PostMapping("/sendnotification/to/searched/people/by")
	public ResponseEntity<?> sendNotificationToSearchedPeople(@RequestParam("candidateId") long candidateId,
			@RequestParam("companyId") long companyId) {

		Map<String, String> sendNotificationToSearchedPeople = this.candidateInboxNotificationService
				.sendNotificationToSearchedPeople(candidateId, companyId);
		return new ResponseEntity<Map<String, String>>(sendNotificationToSearchedPeople, HttpStatus.OK);
	}
	@GetMapping("/shortlisted")
	public ResponseEntity<?> getShortListedCompanyInboxNotificationWithPagination(@RequestParam("candidateId") long candidateId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		CandidateInboxResponseWithPagination trashListCandidateNotifion = this.candidateInboxNotificationService
				.getNotficationListwithPagination(pageNumber, pageSize, candidateId);
		return new ResponseEntity<CandidateInboxResponseWithPagination>(trashListCandidateNotifion, HttpStatus.OK);
	}
}
