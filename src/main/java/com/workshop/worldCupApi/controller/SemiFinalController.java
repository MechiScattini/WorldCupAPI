package com.workshop.worldCupApi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.worldCupApi.entity.SemiFinal;
import com.workshop.worldCupApi.service.SemiFinalService;

@RestController
@RequestMapping("/semiFinal")
public class SemiFinalController {
	@Autowired
	SemiFinalService semiFinalService;
	
	// indexSelecciones endpoint
	@GetMapping()
	public Iterable<SemiFinal> indexSemi(){
		return semiFinalService.getSemiFinals();
	}
	
	// simular partidos de grupos
	@GetMapping("/simularSemiFinal")
	public ResponseEntity<Map<String, String>> simularSemi() {
		return ResponseEntity.status(HttpStatus.OK).body(semiFinalService.simularSemi());
	}
}
