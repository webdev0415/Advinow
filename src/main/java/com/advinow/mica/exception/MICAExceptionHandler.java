package com.advinow.mica.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class MICAExceptionHandler {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@ExceptionHandler(value = { DataInvalidException.class })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public MICAErrorMessage dataInvalidException(Exception ex) {
	   logger.error(ex.getMessage(), ex);
       return new MICAErrorMessage(ex.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY.toString(),ex.getClass().toString(),HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
	  
	   @ExceptionHandler(value = { DataNotFoundException.class })
	    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	    public MICAErrorMessage dataNotFoundException(Exception ex) {
		   logger.error(ex.getMessage(), ex);
	       return new MICAErrorMessage(ex.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY.toString(),ex.getClass().toString(),HttpStatus.UNPROCESSABLE_ENTITY.value());
	    }
	
	   @ExceptionHandler(value = { NoHandlerFoundException.class })
	    @ResponseStatus(HttpStatus.BAD_REQUEST)
	    public MICAErrorMessage noHandlerFoundException(Exception ex) {
		   logger.error(ex.getMessage(), ex);
	        return new MICAErrorMessage(ex.getMessage(),HttpStatus.BAD_REQUEST.toString(),ex.getClass().toString(),HttpStatus.BAD_REQUEST.value());
	    }
	 
	    @ExceptionHandler(value = { DataCreateException.class })
	    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	    public MICAErrorMessage dataCreateException(Exception ex) {
	    	  logger.error(ex.getMessage(), ex);
	        return new MICAErrorMessage(ex.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY.toString(),ex.getClass().toString(),HttpStatus.UNPROCESSABLE_ENTITY.value());
	    }
	    
	    @ExceptionHandler(value = { ServiceNotAvailableException.class })
	    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	    public MICAErrorMessage serviceNotAvailableException(Exception ex) {
	    	  logger.error(ex.getMessage(), ex);
	        return new MICAErrorMessage(ex.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY.toString(),ex.getClass().toString(),HttpStatus.UNPROCESSABLE_ENTITY.value());
	    }

	    @ExceptionHandler(value = { Exception.class })
	    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	    public MICAErrorMessage unknownException(Exception ex) {
	    	  logger.error(ex.getMessage(), ex);
	        return new MICAErrorMessage(ex.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY.toString(),ex.getClass().toString(),HttpStatus.UNPROCESSABLE_ENTITY.value());
	    }

	    
	    
}
