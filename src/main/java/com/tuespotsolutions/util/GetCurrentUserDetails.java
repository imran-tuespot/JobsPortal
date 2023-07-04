package com.tuespotsolutions.util;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.repository.RolesRepository;
import com.tuespotsolutions.repository.UserRepository;

@Component
public class GetCurrentUserDetails {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RolesRepository rolesRepository;

	public String getCurrentUser(Principal principal) {
		User user = userRepository.findByUsername(principal.getName()).orElseThrow(
				() -> new ResourceNotFoundException("User Not Found with username : " + principal.getName()));
		return user.getUsername();
	}

}
