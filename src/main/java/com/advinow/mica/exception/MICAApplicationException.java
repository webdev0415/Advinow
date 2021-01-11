package com.advinow.mica.exception;

public class MICAApplicationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public MICAApplicationException() {
		super();
	}

	public MICAApplicationException(String message, Throwable ex) {
		super(message, ex);
	}

	public MICAApplicationException(String message) {
		super(message);
	}

}
