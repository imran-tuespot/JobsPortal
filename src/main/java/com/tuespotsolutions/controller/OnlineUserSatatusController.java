package com.tuespotsolutions.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.service.OnlineUserSatatusService;

@RestController
@CrossOrigin("*")
@RequestMapping("/checkstatus")
public class OnlineUserSatatusController {

	@Autowired
	private OnlineUserSatatusService onlineUserSatatusService;

	@GetMapping("/user/online")
	public ResponseEntity<?> checkUserOnlineOrNot(@RequestParam("userId") long userId) {
		Map<String, String> livePeopleStatus = this.onlineUserSatatusService.livePeopleStatus(userId);
		return new ResponseEntity<Map<String, String>>(livePeopleStatus, HttpStatus.OK);
	}

}
