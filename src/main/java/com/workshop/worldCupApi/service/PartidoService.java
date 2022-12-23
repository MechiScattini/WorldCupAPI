package com.workshop.worldCupApi.service;

import java.net.URI;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.workshop.worldCupApi.entity.Partido;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.repository.PartidoRepository;

@Service
public class PartidoService {

	@Autowired
	private PartidoRepository partidoRepository;
	
	public Iterable<Partido> getPartidos() {
		return partidoRepository.findAll();
	}
	

	public ResponseEntity<Partido> createPartido(Partido nuevoPartido) {
		try {
			Partido savedPartido = partidoRepository.save(nuevoPartido);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(savedPartido.getId()).toUri();

			return ResponseEntity.created(location).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	// desvincula una seleccion de un partido
	public ResponseEntity<Partido> unsetSeleccionPartido(Partido partido, Seleccion seleccion){
		partido.removeSeleccion(seleccion);
		// siempre recibe un jugador que ya está en la db, por lo tanto createJugador solo actualiza
		return this.createPartido(partido);
	}
	
	public ResponseEntity<?> deletePartido(Long partidoId) {
		try {
			partidoRepository.deleteById(partidoId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			throw new EntityNotFoundException("No se encontró el partido con id: " + partidoId);
		}
	}

}
