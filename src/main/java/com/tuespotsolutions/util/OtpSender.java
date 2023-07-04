package com.tuespotsolutions.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class OtpSender {
	
	public static String sendOtpSms(String mobileNumber, String text, String otp) {
		String url = "https://api.msg91.com/api/sendhttp.php?authkey=331148ADDWswml95ed769e1P1&sender=TUESOT&mobiles=91"+mobileNumber+"&route=4&message=Your login OTP for website "+text+" is "+otp+".&DLT_TE_ID=1207160881189242829&unicode=0";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer xxxxxxxxx");
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate restTemplate = new RestTemplate();
		 ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return exchange.getBody();
	}

}
