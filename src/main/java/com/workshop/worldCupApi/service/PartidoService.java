package com.workshop.worldCupApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.worldCupApi.entity.Partido;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.exceptionss.ForbiddenException;
import com.workshop.worldCupApi.repository.PartidoRepository;

@Service
public class PartidoService {

	@Autowired
	private PartidoRepository partidoRepository;
	
	public Iterable<Partido> getPartidos() {
		return partidoRepository.findAll();
	}

	public Partido createPartido(Partido nuevoPartido) {
		return partidoRepository.save(nuevoPartido);		
	}
	
	// desvincula una seleccion de un partido
	public Partido unsetSeleccionPartido(Partido partido, Seleccion seleccion){
		partido.removeSeleccion(seleccion);
		// siempre recibe un partido que ya está en la db, por lo tanto createPartido solo actualiza
		return this.createPartido(partido);
	}
	
	public void deletePartido(Long partidoId) {
		try {
			partidoRepository.deleteById(partidoId);
		} catch (Exception e) {
			throw new ForbiddenException("Hubo un problema eliminando el partido con id: " + partidoId);
		}
	}

}
