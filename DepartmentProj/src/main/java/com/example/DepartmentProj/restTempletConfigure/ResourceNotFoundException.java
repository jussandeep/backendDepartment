package com.example.DepartmentProj.restTempletConfigure;



import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private HttpStatus httpstatus = HttpStatus.NOT_FOUND;
	private String message;

	public ResourceNotFoundException(String message) {
		super();
		this.message = message;
	}

	public ResourceNotFoundException(HttpStatus httpstatus, String message) {
		super();
		this.httpstatus = httpstatus;
		this.message = message;
	}

	public ResourceNotFoundException(String message, HttpStatus httpstatus) {
		super();
		this.httpstatus = httpstatus;
		this.message = message;
	}

	public HttpStatus getHttpstatus() {
		return httpstatus;
	}

	public void setHttpstatus(HttpStatus httpstatus) {
		this.httpstatus = httpstatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
