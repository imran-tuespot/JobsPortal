package com.tuespotsolutions.email.service;

import java.io.StringWriter;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl {

	@Autowired
	private JavaMailSender emailSender;
	
	Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	public void sendSimpleMessage(String to, String subject, String text, String name,String username, String password) {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(to);
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("name", name);
			velocityContext.put("text", text);
			velocityContext.put("email", to);
			velocityContext.put("username", username);
			velocityContext.put("password", password);
			VelocityEngine engine = new VelocityEngine();
			StringWriter stringWriter = new StringWriter();		
			engine.mergeTemplate("src/main/java/company.vm", "UTF-8", velocityContext, stringWriter);		
			helper.setSubject(subject);			
			helper.setText(stringWriter.toString(),true);
			emailSender.send(message);	
			System.err.println("mail sended !!");
			logger.info("line no : 43 sendSimpleMessage() method, message : mail sended");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("line no : 46 sendSimpleMessage() method "+ e.getMessage());
		}
		
	}

}
