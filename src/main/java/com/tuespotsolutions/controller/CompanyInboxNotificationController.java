package com.tuespotsolutions.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.CompanyInboxNotificationDetails;
import com.tuespotsolutions.models.CompanyInboxNotificationAll;
import com.tuespotsolutions.models.CompanyInboxNotificationAllWithPagination;
import com.tuespotsolutions.models.CompanyInboxPagination;
import com.tuespotsolutions.service.CompanyInboxNotificationService;

@RestController
@CrossOrigin("*")
@RequestMapping("/notification")
public class CompanyInboxNotificationController {

	@Autowired
	private CompanyInboxNotificationService companyInboxNotificationService;

	@GetMapping("/by")
	public ResponseEntity<?> getCompanyInboxNotification(@RequestParam("companyId") long companyId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		CompanyInboxNotificationAllWithPagination notificationList = this.companyInboxNotificationService
				.getNotificationList(companyId,pageNumber,pageSize);
		return new ResponseEntity<CompanyInboxNotificationAllWithPagination>(notificationList, HttpStatus.OK);
	}
	
	@GetMapping("/shortlisted/by")
	public ResponseEntity<?> getShortListedCompanyInboxNotification(@RequestParam("companyId") long companyId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		CompanyInboxNotificationAllWithPagination notificationList = this.companyInboxNotificationService
				.getShortListCompanyInbox(companyId,pageNumber,pageSize);
		return new ResponseEntity<CompanyInboxNotificationAllWithPagination>(notificationList, HttpStatus.OK);
	}
	
	@GetMapping("/trashed/by")
	public ResponseEntity<?> getTrashedCompanyInboxNotification(@RequestParam("companyId") long companyId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		CompanyInboxNotificationAllWithPagination notificationList = this.companyInboxNotificationService
				.getTrashListCompanyInbox(companyId,pageNumber,pageSize);
		return new ResponseEntity<CompanyInboxNotificationAllWithPagination>(notificationList, HttpStatus.OK);
	}

	@GetMapping("/latest/by")
	public ResponseEntity<?> getCompanyInboxNotificationLatest(@RequestParam("companyId") long companyId) {
		List<CompanyInboxNotificationAll> notificationList = this.companyInboxNotificationService
				.getLatestNotification(companyId);
		return new ResponseEntity<List<CompanyInboxNotificationAll>>(notificationList, HttpStatus.OK);
	}

	@GetMapping("/detail/by")
	public ResponseEntity<?> getCompanyInboxNotificationDetail(@RequestParam("notificationId") long notificationId) {
		CompanyInboxNotificationDetails notificationDetails = this.companyInboxNotificationService
				.getNotificationDetails(notificationId);
		return new ResponseEntity<CompanyInboxNotificationDetails>(notificationDetails, HttpStatus.OK);
	}

	@PostMapping("/changestatus")
	public ResponseEntity<?> NotificationSeenStatus(@RequestBody long notificationId){
		this.companyInboxNotificationService.setSeenStatusOnNotifiation(notificationId);
		@SuppressWarnings("unchecked")
		Map<String, String> status = new HashedMap();
		status.put("status", "Notification seen");
		return new ResponseEntity<Map<String, String>>(status, HttpStatus.OK);
	}
	
	@PostMapping("/shortlist")
	public ResponseEntity<?> shortListedInboxNotification(@RequestBody List<Long> notificationId){
		List<CompanyInboxNotificationAll> shortListCompanyInbox = this.companyInboxNotificationService.shortListCompanyInbox(notificationId);
		return new ResponseEntity<List<CompanyInboxNotificationAll>>(shortListCompanyInbox, HttpStatus.OK);
	
	}
	
	@PostMapping("/trashlist")
	public ResponseEntity<?> trashInboxNotification(@RequestBody List<Long> notificationId){
		List<CompanyInboxNotificationAll> trashListCompanyInbox = this.companyInboxNotificationService.trashListCompanyInbox(notificationId);
		return new ResponseEntity<List<CompanyInboxNotificationAll>>(trashListCompanyInbox, HttpStatus.OK);
	
	}
	
	@GetMapping("/shortlisted")
	public ResponseEntity<?> getShortListedCompanyInboxNotificationbyPagination(@RequestParam("companyId") long companyId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		 CompanyInboxPagination allComapanyNotifications = this.companyInboxNotificationService
				.getAllComapanyNotifications(companyId, pageNumber, pageSize);
		return new ResponseEntity<CompanyInboxPagination>(allComapanyNotifications, HttpStatus.OK);
	}
	
}
