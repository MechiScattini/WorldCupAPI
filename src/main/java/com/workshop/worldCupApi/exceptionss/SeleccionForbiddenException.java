package com.workshop.worldCupApi.exceptionss;

public class SeleccionForbiddenException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3630631519387941400L;

	public SeleccionForbiddenException(String errorMsg) {
		super(errorMsg);
	}

}
