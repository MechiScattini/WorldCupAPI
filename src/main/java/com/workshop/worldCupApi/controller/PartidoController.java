package com.workshop.worldCupApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.worldCupApi.entity.Partido;
import com.workshop.worldCupApi.service.PartidoService;

@RestController
@RequestMapping("/partidos")
public class PartidoController {
	
	@Autowired
	private PartidoService partidoService;
	
	@GetMapping()
	public Iterable<Partido> indexPartidos(){
		return partidoService.getPartidos();
	}
	
}
