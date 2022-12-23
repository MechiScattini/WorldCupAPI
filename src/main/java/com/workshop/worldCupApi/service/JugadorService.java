package com.workshop.worldCupApi.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.workshop.worldCupApi.entity.Jugador;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.repository.JugadorRepository;

@Service
public class JugadorService {
	
	@Autowired
	private JugadorRepository jugadorRepository;

	
	// Retorna lista de todos los jugadores
	public Iterable<Jugador> getJugadores() {
		return jugadorRepository.findAll();
	}
	
	public ResponseEntity<Jugador> getJugador(Long jugadorId) {
		Jugador jug = jugadorRepository.findById(jugadorId).orElseThrow(()->new  EntityNotFoundException("No se encontró al jugador con id: "+jugadorId));
		return ResponseEntity.ok().body(jug);
	}

	// Retorna 201 Created si no hay ningun problema, caso contrario devuelve 500 Internal server error
	public ResponseEntity<Jugador> createJugador(Jugador jugador) {
		try {
			Jugador savedJugador = jugadorRepository.save(jugador);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(savedJugador.getId()).toUri();

			return ResponseEntity.created(location).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	// crea jugadores a partir de una lista
	public ResponseEntity<Jugador> createJugadores(List<Jugador> jugadores) {
		for (Jugador jugador : jugadores) {
			this.createJugador(jugador);
		}
		return ResponseEntity.ok().build();
	}
	
	// retorna 204 no content si puede eliminarlo, si falla devuelve 404 not found
	public ResponseEntity<?> deleteJugador(Long jugadorId){
		Optional<Jugador> jugador = jugadorRepository.findById(jugadorId);
		if (jugador.isPresent()) {
			if(jugador.get().getSeleccion() != null) {
				return ResponseEntity
			            .status(HttpStatus.FORBIDDEN)
			            .body("No se puede eliminar al jugador ya que se encuentra en una seleccion");
			}else {
				jugadorRepository.deleteById(jugadorId);
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
		}else {
			throw new EntityNotFoundException("No se encontró al jugador con id: " + jugadorId);
		}
	}
	
	public ResponseEntity<Jugador> setSeleccionJugador(Jugador jugador, Seleccion seleccion){
		jugador.setSeleccion(seleccion);
		// siempre recibe un jugador que ya está en la db, por lo tanto createJugador solo actualiza
		return this.createJugador(jugador);
	}
	
	public ResponseEntity<Jugador> unsetSeleccionJugador(Jugador jugador){
		jugador.setSeleccion(null);
		// siempre recibe un jugador que ya está en la db, por lo tanto createJugador solo actualiza
		return this.createJugador(jugador);
	}
	
}
