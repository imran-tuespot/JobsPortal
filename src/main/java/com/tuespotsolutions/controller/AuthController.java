package com.tuespotsolutions.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.tuespotsolutions.customexception.OtpNotSendedException;
import com.tuespotsolutions.entity.OnlinePeopleStatus;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.repository.OnlinePeopleStatusRepository;
import com.tuespotsolutions.repository.RolesRepository;
import com.tuespotsolutions.repository.UserRepository;
import com.tuespotsolutions.security.configuration.JwtUtils;
import com.tuespotsolutions.security.model.LoginRequest;
import com.tuespotsolutions.security.model.LoginResponse;
import com.tuespotsolutions.security.service.UserDetailsImpl;
import com.tuespotsolutions.util.GetCurrentUserDetails;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

	Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RolesRepository rolesRepository;
	
	@Autowired
	GetCurrentUserDetails currentUserDetails;
	
	@Autowired
	OnlinePeopleStatusRepository onlinePeopleStatusRepository;

	@Autowired
	JwtUtils jwtUtils;
	
	
	@PostMapping("/signin")
	  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		logger.info("line no 55 authenticateUser() metho");
	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);
	    
	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
	    List<String> roles = userDetails.getAuthorities().stream()
	        .map(item -> item.getAuthority())
	        .collect(Collectors.toList());

	    Optional<User> findById = this.userRepository.findById(userDetails.getId());
	    
	 
	    		if(findById.get().isStatus()) {
	    			logger.info("line no 71 return user info authenticateUser() method");
	    			
	    		
	    			if(findById.get().getUserType().contains("CANDIDATE")) {
	    				
	    				
	    				Optional<OnlinePeopleStatus> optional = onlinePeopleStatusRepository.findByUserId(findById.get().getId());
	    				
	    				if(optional.isPresent()) {
	    					java.util.Date utilDate = new java.util.Date();
			    			TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
			    			SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    			timeStamp.setTimeZone(istTimeZone);
	    					optional.get().setLastSeen(utilDate);
	    					onlinePeopleStatusRepository.save(optional.get());
	    				}else {
	    					OnlinePeopleStatus onlinePeopleStatus = new OnlinePeopleStatus();
			    			onlinePeopleStatus.setStatus(true);
			    			onlinePeopleStatus.setUserId(userDetails.getId());
			    			java.util.Date utilDate = new java.util.Date();
			    			TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
			    			SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    			timeStamp.setTimeZone(istTimeZone);
			    			onlinePeopleStatus.setLastSeen(utilDate);	
			    			onlinePeopleStatusRepository.save(onlinePeopleStatus);
	    				}
	    			}
	    			
	    			  return ResponseEntity.ok(new LoginResponse(jwt, 
		                         userDetails.getId(), 
		                         userDetails.getUsername(), 
		                         userDetails.getEmail(),
		                         findById.get().getTypeId(),
		                         roles));
	    		}else {
	    			logger.info("line no 78 Throw Otp process pending exception authenticateUser() method");
	    			throw new OtpNotSendedException("Otp process is not completed. Kindly complete the otp process");
	    		}    
	  
	  }
	
	
	@GetMapping("/currentuser")
	public ResponseEntity<?> currentUserName(Principal principal){
		String currentUser = this.currentUserDetails.getCurrentUser(principal);
		@SuppressWarnings("unchecked")
		Map<String, String> resp = new HashedMap();
		resp.put("username", currentUser);
		return new ResponseEntity<Map<String, String>>(resp,HttpStatus.OK);
	}
	

}
