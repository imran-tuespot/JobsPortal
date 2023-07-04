package com.tuespotsolutions.security.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

	@NotEmpty(message = "Username field is required")
	private String username;

	@NotEmpty(message = "Password field is required")
	@Size(min = 3, message = "Password must be atleast minimum of 3 characters")
	private String password;

	

}
