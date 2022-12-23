package com.workshop.worldCupApi.controller;

import java.util.List;

//import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.worldCupApi.entity.Jugador;
import com.workshop.worldCupApi.service.JugadorService;

@Validated
@RestController
@RequestMapping("/jugadores")
public class JugadorController {
	
	@Autowired
	private JugadorService jugadorService;
	
	// getJugadores endpoint
	@GetMapping()
	public Iterable<Jugador> indexJugadores(){
		return jugadorService.getJugadores();
	}
	
	// getJugador endpoint
	@GetMapping("/{id}")
	public ResponseEntity<Jugador> showJugador(@PathVariable("id") Long jugadorId){
		return jugadorService.getJugador(jugadorId);
	}
	
	// createJugador endpoint
	@PostMapping()
	public ResponseEntity<Jugador> createJugador(@RequestBody Jugador jugador) {
		return jugadorService.createJugador(jugador);
	}
	
	// createJugadores endpoint
	@PostMapping("/lista")
	public ResponseEntity<Jugador> createJugadores(@RequestBody List<Jugador> jugadores) {
		return jugadorService.createJugadores(jugadores);
	}
	
	// deleteJugador endpoint
	@DeleteMapping("/{id}")
	public ResponseEntity<?> destroyJugador(@PathVariable("id") Long jugadorId){
		return jugadorService.deleteJugador(jugadorId);
	}
	
}
