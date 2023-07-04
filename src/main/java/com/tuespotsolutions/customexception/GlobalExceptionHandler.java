package com.tuespotsolutions.customexception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiExceptionResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {
		logger.info("line no : 21 resourceNotFoundExceptionHandler() method, message : "+exception.getMessage());
		return new ResponseEntity<ApiExceptionResponse>(new ApiExceptionResponse(exception.getMessage(), false),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException exception) {
		logger.info("line no : 30 handleMethodArgumentNotValidException() method, message : "+exception.getMessage());
		Map<String, String> resp = new HashMap<String, String>();
		exception.getBindingResult().getAllErrors().forEach(errors -> {
			String fieldName = ((FieldError) errors).getField();
			String defaultMessage = errors.getDefaultMessage();
			resp.put(fieldName, defaultMessage);
		});
		
		
		return new ResponseEntity<Map<String, String>>(resp, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(FieldAlreadyExistException.class)
	public ResponseEntity<ApiExceptionResponse> handleEmailAlreadyExistException(
			FieldAlreadyExistException emailAlreadyExistException) {
		logger.info("line no : 43 handleEmailAlreadyExistException() method, message : "+emailAlreadyExistException.getMessage());
		return new ResponseEntity<ApiExceptionResponse>(
				new ApiExceptionResponse(emailAlreadyExistException.getMessage(), false), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidFileFormatException.class)
	public ResponseEntity<ApiExceptionResponse> handleInvalidFileFormatException(InvalidFileFormatException exception){
		logger.info("line no : 50 handleInvalidFileFormatException() method, message : "+exception.getMessage());
		return new ResponseEntity<ApiExceptionResponse>(
				new ApiExceptionResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(OtpNotSendedException.class)
	public ResponseEntity<ApiExceptionResponse> handleOtpNotSendedException(OtpNotSendedException exception){
		logger.info("line no : 57 handleOtpNotSendedException() method, message : "+exception.getMessage());
		return new ResponseEntity<ApiExceptionResponse>(
				new ApiExceptionResponse(exception.getMessage(), false), HttpStatus.EXPECTATION_FAILED);
	}
	
	
	//WrongOtpException
	@ExceptionHandler(WrongOtpException.class)
	public ResponseEntity<ApiExceptionResponse> handleWrongOtpException(WrongOtpException exception){
		logger.info("line no : 66 handleWrongOtpException() method, message : "+exception.getMessage());
		return new ResponseEntity<ApiExceptionResponse>(
				new ApiExceptionResponse(exception.getMessage(), false), HttpStatus.EXPECTATION_FAILED);
	}
	
	//NullFieldException
	@ExceptionHandler(NullFieldException.class)
	public ResponseEntity<ApiExceptionResponse> handleNullFieldException(NullFieldException exception){
		logger.info("line no : 76 handleNullFieldException() method, message : "+exception.getMessage());
		return new ResponseEntity<ApiExceptionResponse>(
				new ApiExceptionResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);
	}
}
