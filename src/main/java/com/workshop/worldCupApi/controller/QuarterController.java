package com.workshop.worldCupApi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.worldCupApi.entity.Quarter;
import com.workshop.worldCupApi.service.QuarterService;

@RestController
@RequestMapping("/cuartos")
public class QuarterController {
	@Autowired
	QuarterService quarterService;
	
	// indexSelecciones endpoint
	@GetMapping()
	public Iterable<Quarter> indexQuarters(){
		return quarterService.getQuarter();
	}
	
	// simular partidos de grupos
	@GetMapping("/simularQuarters")
	public ResponseEntity<Map<String, String>> simularQuarters() {
		return ResponseEntity.status(HttpStatus.OK).body(quarterService.simularQuarters());
	}
}
