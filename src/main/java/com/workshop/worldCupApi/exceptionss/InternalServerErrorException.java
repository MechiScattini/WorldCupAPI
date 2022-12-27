package com.workshop.worldCupApi.exceptionss;

public class InternalServerErrorException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8817682217761733571L;
	
	public InternalServerErrorException (String errorMsg) {
		super(errorMsg);
	}
	
}
