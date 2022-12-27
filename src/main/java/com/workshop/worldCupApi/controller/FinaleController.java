package com.workshop.worldCupApi.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.worldCupApi.entity.Finale;
import com.workshop.worldCupApi.service.FinaleService;

@RestController
@RequestMapping("/final")
public class FinaleController {
	@Autowired
	FinaleService finalService;
	
	// indexSelecciones endpoint
	@GetMapping()
	public Iterable<Finale> indexFinal(){
		return finalService.getFinals();
	}
	
	// simular partidos de grupos
	@GetMapping("/simularFinal")
	public ResponseEntity<Map<String, String>> simularFinal() {
		return ResponseEntity.status(HttpStatus.OK).body(finalService.simularFinal());
	}
}
