package com.tuespotsolutions.customexception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullFieldException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger logger = LoggerFactory.getLogger(NullFieldException.class);

	public NullFieldException(String message) {
		super(message);
		logger.error("line no 17 NullFieldException, message : "+message);
	}
	
	

}
