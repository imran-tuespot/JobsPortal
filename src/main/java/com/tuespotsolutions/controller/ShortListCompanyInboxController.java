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

import com.tuespotsolutions.models.ShortListCompanyInboxDetail;
import com.tuespotsolutions.service.ShortListCompanyInboxService;

@RestController
@CrossOrigin("*")
@RequestMapping("/companynotification")
public class ShortListCompanyInboxController {

	@Autowired
	private ShortListCompanyInboxService shortListCompanyInboxService;

	@PostMapping("/shortlist")
	public ResponseEntity<?> shortListCompanyNotification(@RequestBody List<Long> notificationId) {
		List<ShortListCompanyInboxDetail> shortListCompanyNotifications = this.shortListCompanyInboxService
				.shortListCompanyNotifications(notificationId);
		return new ResponseEntity<List<ShortListCompanyInboxDetail>>(shortListCompanyNotifications, HttpStatus.CREATED);
	}

	@GetMapping("/get/shotlisted")
	public ResponseEntity<?> getShortListCompanyNotification(@RequestParam("companyId") long companyId) {
		List<ShortListCompanyInboxDetail> shortListCompanyNotificationListByCompanyId = this.shortListCompanyInboxService
				.getShortListCompanyNotificationListByCompanyId(companyId);
		return new ResponseEntity<List<ShortListCompanyInboxDetail>>(shortListCompanyNotificationListByCompanyId,
				HttpStatus.CREATED);
	}

	@PostMapping("/changestatus")
	public ResponseEntity<?> NotificationSeenStatus(@RequestBody long notificationId){
		this.shortListCompanyInboxService.setSeenStatusOnNotifiation(notificationId);
		@SuppressWarnings("unchecked")
		Map<String, String> status = new HashedMap();
		status.put("status", "Notification seen");
		return new ResponseEntity<Map<String, String>>(status, HttpStatus.OK);
	}
	
}
