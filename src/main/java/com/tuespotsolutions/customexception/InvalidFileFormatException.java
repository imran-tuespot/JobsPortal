package com.tuespotsolutions.customexception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidFileFormatException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger(InvalidFileFormatException.class);
	
	public InvalidFileFormatException(String message) {
		super(message);
		logger.info("line no : 17 InvalidFileFormatException() method, message : "+message);
	}
	
}
