package com.workshop.worldCupApi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.worldCupApi.entity.Partido;
import com.workshop.worldCupApi.entity.Quarter;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.repository.QuarterRepository;

@Service
public class QuarterService {
	
	@Autowired
	QuarterRepository quarterRepository;
	
	@Autowired
	SemiFinalService semiFinalService;
	
	@Autowired
	SeleccionService seleccionService;

	public Iterable<Quarter> getQuarter() {
		return quarterRepository.findAll();
	}
	
	public Quarter createQuarter(Seleccion seleccion1, Seleccion seleccion2) {
		
		Quarter llaveQuarter = new Quarter();
		llaveQuarter.addSeleccion(seleccion1);
		llaveQuarter.addSeleccion(seleccion2);
		
		return quarterRepository.save(llaveQuarter);
	}
	
	public void generarLlaves(List<Seleccion> selecciones) {
			
		Collections.shuffle(selecciones);
		
		for(int i=0; i < selecciones.size(); i+=2) {
			
			Quarter llaveQuarter = this.createQuarter(selecciones.get(i),selecciones.get(i+1));
			seleccionService.setSeleccionQuarter(selecciones.get(i), llaveQuarter);
			seleccionService.setSeleccionQuarter(selecciones.get(i+1), llaveQuarter);
		}
		
	}


	public Map<String, String> simularQuarters() {
		Map<String, String> resultado = new HashMap<>();
		
		// esta lista se usaria para generar los cuartos de final
		List<Seleccion> listQuartersWinners = new ArrayList<>();
		
		List<Quarter> quarters = quarterRepository.findAll();
		
		for(Quarter llaveQuarters : quarters) {
			Seleccion seleccion1 = llaveQuarters.getSelecciones().get(0);
			Seleccion seleccion2 = llaveQuarters.getSelecciones().get(1);
			Partido partidoJugado = seleccionService.jugarPartido(seleccion1.getId(), seleccion2.getId());
			
			// setea el resultado del partido a la llave de octavos correspondiente y lo guarda
			llaveQuarters.setResultado(partidoJugado.getResultado());
			quarterRepository.save(llaveQuarters);
			
			// se fija quien ganó el partido y agrega esa seleccion a la lista de ganadores de los octavos
			if(partidoJugado.getResultado().contains(seleccion1.getPais())) {
				listQuartersWinners.add(seleccion1);
			}else {
				listQuartersWinners.add(seleccion2);
			}
			
			// agrega el resultado al hash que devuelve la api
			resultado.put(seleccion1.getPais() +"-"+ seleccion2.getPais() +": ", partidoJugado.getResultado());
			
		}
		
		
		// genera los cuartos de final con la lista de ganadores
		semiFinalService.generarLlaves(listQuartersWinners);
		
		return resultado;
	}

}
