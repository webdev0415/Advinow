package com.advinow.mica.exception;


public class MICAErrorMessage {

	private String message;
	private String status;
	private String reason;
	private int statusCode;

	/**
	 * @param message
	 * 
	 *  
	 */
	public MICAErrorMessage(String message) {
		this.message = message;
	}
	
	
	/**
	 * @param message
	 * @param status
	 *  
	 */
	public MICAErrorMessage(String message, String status) {
		this.message = message;
		this.status = status;
	}
	
	
	/**
	 * @param message
	 * @param status
	 * @param reason
	 * 
	 */
	public MICAErrorMessage(String message, String status, String reason) {
		this.message = message;
		this.status = status;
		this.reason = reason;
	
	}
	
	
	/**
	 * @param message
	 * @param status
	 * @param reason
	 * @param statusCode
	 */
	public MICAErrorMessage(String message, String status, String reason,
		int statusCode) {
		this.message = message;
		this.status = status;
		this.reason = reason;
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
}
