package com.workshop.worldCupApi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.worldCupApi.entity.Jugador;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.exceptionss.ForbiddenException;
import com.workshop.worldCupApi.exceptionss.NotFoundException;
import com.workshop.worldCupApi.repository.JugadorRepository;

@Service
public class JugadorService {
	
	@Autowired
	private JugadorRepository jugadorRepository;

	
	// Retorna lista de todos los jugadores
	public Iterable<Jugador> getJugadores() {
		return jugadorRepository.findAll();
	}
	
	public Jugador getJugador(Long jugadorId) throws NotFoundException {
		return jugadorRepository.findById(jugadorId).orElseThrow(()->new  NotFoundException("No se encontró al jugador con id: "+jugadorId));
	}

	// Retorna 201 Created si no hay ningun problema, caso contrario devuelve 500 Internal server error
	public Jugador createJugador(Jugador jugador) {
		return jugadorRepository.save(jugador);
	}
	
	// crea jugadores a partir de una lista
	public void createJugadores(List<Jugador> jugadores) {
		for (Jugador jugador : jugadores) {
			this.createJugador(jugador);
		}
	}
	
	// retorna 204 no content si puede eliminarlo, si falla devuelve 404 not found
	public void deleteJugador(Long jugadorId){
		Jugador jugador = this.getJugador(jugadorId);
		if(jugador.getSeleccion() != null) {
			throw new ForbiddenException("No se puede eliminar al jugador ya que se encuentra en una seleccion");
		}else {
			try {
				jugadorRepository.deleteById(jugadorId);
			}
			catch (Exception e) {
				throw new ForbiddenException("Hubo un problema al intentar eliminar al jugador con id: " + jugadorId);
			}
		}
	}
	
	public Jugador setSeleccionJugador(Jugador jugador, Seleccion seleccion){
		jugador.setSeleccion(seleccion);
		// siempre recibe un jugador que ya está en la db, por lo tanto createJugador solo actualiza
		return this.createJugador(jugador);
	}
	
	public Jugador unsetSeleccionJugador(Jugador jugador){
		jugador.setSeleccion(null);
		// siempre recibe un jugador que ya está en la db, por lo tanto createJugador solo actualiza
		return this.createJugador(jugador);
	}
	
}
