package com.tuespotsolutions.security.model;

import java.util.List;

import lombok.Data;

@Data
public class LoginResponse {

	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String email;
	private Long userId;
	private List<String> roles;
	
	public LoginResponse(String token,Long id, String username, String email, Long userId,
			List<String> roles) {
		super();
		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.userId = userId;
		this.roles = roles;
	}

	

	
}
