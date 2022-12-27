package com.workshop.worldCupApi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.worldCupApi.entity.Octavos;
import com.workshop.worldCupApi.service.OctavosService;

@RestController
@RequestMapping("/octavos")
public class OctavosController {
	
	@Autowired
	OctavosService octavosService;
	
	// indexSelecciones endpoint
	@GetMapping()
	public Iterable<Octavos> indexOctavos(){
		return octavosService.getOctavos();
	}
	
	// simular partidos de grupos
	@GetMapping("/simularOctavos")
	public ResponseEntity<Map<String, String>> simularOctavos() {
		return ResponseEntity.status(HttpStatus.OK).body(octavosService.simularOctavos());
	}

}
