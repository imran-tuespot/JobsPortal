package com.tuespotsolutions.customexception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtpNotSendedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger(OtpNotSendedException.class);
	
	public OtpNotSendedException(String message) {
		super(message);
		logger.info("line no : 17 OtpNotSendedException() method, message : "+message);
	}
		
}
