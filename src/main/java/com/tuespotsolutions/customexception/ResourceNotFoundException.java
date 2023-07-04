package com.tuespotsolutions.customexception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);

	public ResourceNotFoundException(String message) {
		super(message);
		logger.info("line no : 17 ResourceNotFoundException() method, message : "+message);
		
	}
	
}
