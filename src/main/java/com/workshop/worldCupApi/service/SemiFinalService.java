package com.workshop.worldCupApi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.worldCupApi.entity.Partido;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.entity.SemiFinal;
import com.workshop.worldCupApi.repository.SemiFinalRepository;

@Service
public class SemiFinalService {
	
	@Autowired
	SemiFinalRepository semiFinalRepository;
	
	@Autowired
	SeleccionService seleccionService;
	
	@Autowired
	FinaleService finalService;

	public Iterable<SemiFinal> getSemiFinals() {
		return semiFinalRepository.findAll();
	}
	
	public SemiFinal createSemiFinal(Seleccion seleccion1, Seleccion seleccion2) {
		
		SemiFinal llaveSemiFinal = new SemiFinal();
		llaveSemiFinal.addSeleccion(seleccion1);
		llaveSemiFinal.addSeleccion(seleccion2);
		
		return semiFinalRepository.save(llaveSemiFinal);
	}
	
	public void generarLlaves(List<Seleccion> selecciones) {
		
		
		//List<Seleccion> listSeleccionesShufleada = new ArrayList<Seleccion>();
		
		//listSeleccionesShufleada = this.shuffleWithCondition2(listSeleccionesShufleada, selecciones);
		
		
		Collections.shuffle(selecciones);
		
		for(int i=0; i < selecciones.size(); i+=2) {
			
			SemiFinal llaveSemiFinal = this.createSemiFinal(selecciones.get(i),selecciones.get(i+1));
			seleccionService.setSeleccionSemiFinal(selecciones.get(i), llaveSemiFinal);
			seleccionService.setSeleccionSemiFinal(selecciones.get(i+1), llaveSemiFinal);
		}
		
	}
	

	public Map<String, String> simularSemi() {
		Map<String, String> resultado = new HashMap<>();
		
		// esta lista se usaria para generar los cuartos de final
		List<Seleccion> listSemiFinalWinners = new ArrayList<Seleccion>();
		
		List<SemiFinal> semiFinals = semiFinalRepository.findAll();
		
		for(SemiFinal llaveSemiFinal : semiFinals) {
			Seleccion seleccion1 = llaveSemiFinal.getSelecciones().get(0);
			Seleccion seleccion2 = llaveSemiFinal.getSelecciones().get(1);
			Partido partidoJugado = seleccionService.jugarPartido(seleccion1.getId(), seleccion2.getId());
			
			// setea el resultado del partido a la llave de octavos correspondiente y lo guarda
			llaveSemiFinal.setResultado(partidoJugado.getResultado());
			semiFinalRepository.save(llaveSemiFinal);
			
			// se fija quien ganó el partido y agrega esa seleccion a la lista de ganadores de los octavos
			if(partidoJugado.getResultado().contains(seleccion1.getPais())) {
				listSemiFinalWinners.add(seleccion1);
			}else {
				listSemiFinalWinners.add(seleccion2);
			}
			
			// agrega el resultado al hash que devuelve la api
			resultado.put(seleccion1.getPais() +"-"+ seleccion2.getPais() +": ", partidoJugado.getResultado());
			
		}
		
		
		// genera los cuartos de final con la lista de ganadores
		finalService.generarLlaves(listSemiFinalWinners);
		
		return resultado;
	}

}
