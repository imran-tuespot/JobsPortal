package com.tuespotsolutions.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.entity.Roles;
import com.tuespotsolutions.service.RolesService;

@RestController
@CrossOrigin("*")
@RequestMapping("/roles")
public class RolesController {

	Logger logger = LoggerFactory.getLogger(RolesController.class);
	
	@Autowired
	private RolesService rolesService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addRoles(){
		 logger.info("line no : 31 addRoles() method");
		 List<Roles> saveRoles = this.rolesService.saveRoles();
		return new ResponseEntity<List<Roles>>(saveRoles, HttpStatus.CREATED);
	}
	
}
