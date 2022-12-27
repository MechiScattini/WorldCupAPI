package com.workshop.worldCupApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
		Jugador jugador = jugadorService.getJugador(jugadorId); 
		return ResponseEntity.ok().body(jugador);
	}
	
	// createJugador endpoint
	@PostMapping()
	public ResponseEntity<Jugador> createJugador(@RequestBody Jugador jugador) {
		Jugador newJugador = jugadorService.createJugador(jugador);
		return ResponseEntity.ok(newJugador);
	}
	
	// createJugadores endpoint
	@PostMapping("/lista")
	public ResponseEntity<String> createJugadores(@RequestBody List<Jugador> jugadores) {
		jugadorService.createJugadores(jugadores);
		return new ResponseEntity<>("Jugador creado exitosamente",HttpStatus.CREATED);
	}
	
	// deleteJugador endpoint
	@DeleteMapping("/{id}")
	public ResponseEntity<?> destroyJugador(@PathVariable("id") Long jugadorId){
		jugadorService.deleteJugador(jugadorId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
