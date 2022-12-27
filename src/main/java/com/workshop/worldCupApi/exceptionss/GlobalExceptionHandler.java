package com.workshop.worldCupApi.exceptionss;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
		@ExceptionHandler(NotFoundException.class)
		protected ResponseEntity<Object> handleEntityNotFound(
				NotFoundException ex) {
		    ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
		    apiError.setMessage(ex.getMessage());
		    return buildResponseEntity(apiError);
		}
		
		@ExceptionHandler(ForbiddenException.class)
		protected ResponseEntity<Object> handleSeleccionNotCreated(
				ForbiddenException ex) {
		    ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
		    apiError.setMessage(ex.getMessage());
		    return buildResponseEntity(apiError);
		}
		
		@ExceptionHandler(InternalServerErrorException.class)
		protected ResponseEntity<Object> handleSeleccionNotCreated(
				InternalServerErrorException ex) {
		    ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		    apiError.setMessage(ex.getMessage());
		    return buildResponseEntity(apiError);
		}
		
		 // helper para armar la response entity con la apiError
		private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		   return new ResponseEntity<>(apiError, apiError.getStatus());
		}
}

