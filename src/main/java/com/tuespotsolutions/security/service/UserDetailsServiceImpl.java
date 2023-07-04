package com.tuespotsolutions.security.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 User user = this.userRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User Not Found with username: "+username));
		System.err.println(user.toString());
		logger.info("line no 28 loadUserByUsername() method");
		 return UserDetailsImpl.build(user);
	}

}
