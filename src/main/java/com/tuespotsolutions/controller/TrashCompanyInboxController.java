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

import com.tuespotsolutions.models.TrashCompanyInboxDetail;
import com.tuespotsolutions.service.TrashCompanyInboxService;

@RestController
@CrossOrigin("*")
@RequestMapping("/tashcompanyinbox")
public class TrashCompanyInboxController {

	@Autowired
	private TrashCompanyInboxService trashCompanyInboxService;

	@PostMapping("/trashed")
	public ResponseEntity<?> trashCompanyInboxNottification(@RequestBody List<Long> notificationId) {
		List<TrashCompanyInboxDetail> trashListCompanyNotifications = this.trashCompanyInboxService
				.trashListCompanyNotifications(notificationId);
		return new ResponseEntity<List<TrashCompanyInboxDetail>>(trashListCompanyNotifications, HttpStatus.CREATED);
	}
	
	@GetMapping("/get/trashed/list")
	public ResponseEntity<?> getTrashedCompanyInboxNotificationListByCompanyId(@RequestParam("companyId") long companyId){
		List<TrashCompanyInboxDetail> trashListCompanyNotificationListByCompanyId = this.trashCompanyInboxService.getTrashListCompanyNotificationListByCompanyId(companyId);
		return new ResponseEntity<List<TrashCompanyInboxDetail>>(trashListCompanyNotificationListByCompanyId, HttpStatus.CREATED);
	}
	
	@PostMapping("/changestatus")
	public ResponseEntity<?> trashNotificationSeenStatus(@RequestBody long notificationId){
		this.trashCompanyInboxService.setSeenStatusOnNotifiation(notificationId);
		@SuppressWarnings("unchecked")
		Map<String, String> status = new HashedMap();
		status.put("status", "Notification seen");
		return new ResponseEntity<Map<String, String>>(status, HttpStatus.OK);
	}

	@PostMapping("/shortlist/to/trashed")
	public ResponseEntity<?> shortListToTrashCompanyInboxNottification(@RequestBody List<Long> notificationId) {
		List<TrashCompanyInboxDetail> trashListCompanyNotifications = this.trashCompanyInboxService
				.trashShortedListCompanyNotifications(notificationId);
		return new ResponseEntity<List<TrashCompanyInboxDetail>>(trashListCompanyNotifications, HttpStatus.CREATED);
	}
}
