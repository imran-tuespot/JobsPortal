package com.tuespotsolutions.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpConfirmedRequest {
	
	private Long otpId;
	private String otp;
	

}
