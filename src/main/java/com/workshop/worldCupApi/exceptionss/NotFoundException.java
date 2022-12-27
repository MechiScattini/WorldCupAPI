package com.workshop.worldCupApi.exceptionss;

public class NotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1304310311819949341L;
	public NotFoundException(String errorMsg) {
		super(errorMsg);
	}
}
