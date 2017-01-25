package com.rest.crawler;

public class ReturnObject {
	
	public Status status;
	public Object data;
	public String description;
	
	
	public ReturnObject (Status status, Object object, String description) {
		
		this.status = status;
		this.data = object;
		this.description = description;
	}
	
	
	public enum Status {
		
		OK,
		ERROR,
		FAILED
	}

}
