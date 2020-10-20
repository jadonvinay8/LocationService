package com.capgemini.LocationService.beans;

public class ErrorResponse {

	private String errorMsg;
	private String cause;
	private String code;
	
	public ErrorResponse() {
		// Default Constructor
	}

	public ErrorResponse(String errorMsg, String exception, String code) {
		this.errorMsg = errorMsg;
		this.cause = exception;
		this.code = code;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
}
