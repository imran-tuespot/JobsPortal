package com.tuespotsolutions.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin("*")
@RequestMapping("/otp")
public class SmsController {
	
	Logger logger = LoggerFactory.getLogger(SmsController.class);
	
	@GetMapping("/send")
	public String sendOtp() {
		logger.info("line no : 25 sendOtp() method");
		String url = "https://api.msg91.com/api/sendhttp.php?authkey=331148ADDWswml95ed769e1P1&sender=TUESOT&mobiles=919597151742&route=4&message=Your login OTP for website jobsearch is 123456.&DLT_TE_ID=1207160881189242829&unicode=0";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer xxxxxxxxx");
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate restTemplate = new RestTemplate();
		 ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return exchange.getBody();
	}

}
