package com.tuespotsolutions.customexception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FieldAlreadyExistException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger logger = LoggerFactory.getLogger(FieldAlreadyExistException.class);

	public FieldAlreadyExistException(String message) {
		super(message);
		logger.info("line no : 31 FieldAlreadyExistException() method, message : "+message);
		
	}
	
	
	

}
