package com.workshop.worldCupApi.exceptionss;

public class ForbiddenException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3630631519387941400L;

	public ForbiddenException(String errorMsg) {
		super(errorMsg);
	}

}
