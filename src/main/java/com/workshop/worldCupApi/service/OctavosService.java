package com.workshop.worldCupApi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.worldCupApi.entity.Octavos;
import com.workshop.worldCupApi.entity.Partido;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.repository.OctavosRepository;

@Service
public class OctavosService {
	
	@Autowired
	OctavosRepository octavosRepository;
	
	@Autowired
	QuarterService quarterService;
	
	@Autowired
	SeleccionService seleccionService;
	
	public Iterable<Octavos> getOctavos(){
		return octavosRepository.findAll();
	}

	public Octavos createOctavos(Seleccion seleccion1, Seleccion seleccion2) {
		
		Octavos llaveOctavos = new Octavos();
		llaveOctavos.addSeleccion(seleccion1);
		llaveOctavos.addSeleccion(seleccion2);
		
		return octavosRepository.save(llaveOctavos);
	}
	
	// genera llaves de octavos aleatorias
	public void generarLlaves(List<Seleccion> selecciones) {
		
		
		//List<Seleccion> listSeleccionesShufleada = new ArrayList<Seleccion>();
		
		//listSeleccionesShufleada = this.shuffleWithCondition2(listSeleccionesShufleada, selecciones);
		
		
		Collections.shuffle(selecciones);
		
		for(int i=0; i < selecciones.size(); i+=2) {
			
			Octavos llaveOctavos = this.createOctavos(selecciones.get(i),selecciones.get(i+1));
			seleccionService.setSeleccionOctavos(selecciones.get(i), llaveOctavos);
			seleccionService.setSeleccionOctavos(selecciones.get(i+1), llaveOctavos);
		}
		
	}
	
	public List<Seleccion> shuffleWithCondition2(List<Seleccion> seleccionesResult, List<Seleccion> seleccionesGiven){
		
		List<Seleccion> shuffleList = new ArrayList<Seleccion>();
		
		for(int i=0; i < seleccionesGiven.size(); i+=2) {
			boolean ok = false;
			int k = i + 1;
			while(!ok) {
				if (this.canPlayWith(seleccionesGiven.get(i), seleccionesGiven.get(k))) {
					shuffleList.add(seleccionesGiven.get(i));
					shuffleList.add(seleccionesGiven.get(k));
					ok = true;
				} else {
					k ++;
				}
			}
		}
		return shuffleList;
	}
	
	public List<Seleccion> shuffleWithCondition(List<Seleccion> seleccionesResult, List<Seleccion> seleccionesGiven){
		if(seleccionesGiven.isEmpty()) {
			return seleccionesResult;
		} 
		for( int i=0; i < seleccionesGiven.size()-1; i++) {
			if(this.canPlayWith(seleccionesGiven.get(i), seleccionesGiven.get(i+1))) {
				seleccionesResult.add(seleccionesGiven.get(i));
				seleccionesResult.add(seleccionesGiven.get(i+1));
				seleccionesGiven.remove(0);
				if(seleccionesGiven.size()>1) {
					seleccionesGiven.remove(1);
				} 
				List<Seleccion> r = shuffleWithCondition(seleccionesResult, seleccionesGiven);
				if(r != null) {
					return r;
				}
			}
		}
		return null;
	}
	
	public boolean canPlayWith(Seleccion seleccion1, Seleccion seleccion2) {
		if(seleccion1.getGroup().getGroup() == seleccion2.getGroup().getGroup()){
			return false;
		} else {
			return true;
		}
	}
	
	// simula los partidos de octavos
	public Map<String, String> simularOctavos(){
		Map<String, String> resultado = new HashMap<>();
		
		// esta lista se usaria para generar los cuartos de final
		List<Seleccion> listOctavosWinners = new ArrayList<Seleccion>();
		
		List<Octavos> octavos = octavosRepository.findAll();
		
		for(Octavos llaveOctavos : octavos) {
			Seleccion seleccion1 = llaveOctavos.getSelecciones().get(0);
			Seleccion seleccion2 = llaveOctavos.getSelecciones().get(1);
			Partido partidoJugado = seleccionService.jugarPartido(seleccion1.getId(), seleccion2.getId());
			
			// setea el resultado del partido a la llave de octavos correspondiente y lo guarda
			llaveOctavos.setResultado(partidoJugado.getResultado());
			octavosRepository.save(llaveOctavos);
			
			// se fija quien ganó el partido y agrega esa seleccion a la lista de ganadores de los octavos
			if(partidoJugado.getResultado().contains(seleccion1.getPais())) {
				listOctavosWinners.add(seleccion1);
			}else {
				listOctavosWinners.add(seleccion2);
			}
			
			// agrega el resultado al hash que devuelve la api
			resultado.put(seleccion1.getPais() +"-"+ seleccion2.getPais() +": ", partidoJugado.getResultado());
			
		}
		
		// genera los cuartos de final con la lista de ganadores
		quarterService.generarLlaves(listOctavosWinners);
		
		return resultado;
	}
	
}
