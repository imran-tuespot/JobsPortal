package com.tuespotsolutions.customexception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WrongOtpException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger logger = LoggerFactory.getLogger(WrongOtpException.class);

	public WrongOtpException(String message) {
		super(message);
		logger.info("line no : 17 WrongOtpException() method, message : "+message);
	}
	
	

}
