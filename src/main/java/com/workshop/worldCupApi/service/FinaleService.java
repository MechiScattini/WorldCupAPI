package com.workshop.worldCupApi.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.worldCupApi.entity.Finale;
import com.workshop.worldCupApi.entity.Partido;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.repository.FinaleRepository;

@Service
public class FinaleService {
	
	@Autowired
	FinaleRepository finalRepository;
	
	@Autowired
	SeleccionService seleccionService;

	public Iterable<Finale> getFinals() {
		return finalRepository.findAll();
	}
	
	public Finale createFinal(Seleccion seleccion1, Seleccion seleccion2) {
		Finale llaveFinal = new Finale(seleccion1,seleccion2);
		
		return finalRepository.save(llaveFinal);
	}
	
	public void generarLlaves(List<Seleccion> selecciones) {
		
		Collections.shuffle(selecciones);
		
		for(int i=0; i < selecciones.size(); i+=2) {
			
			Finale llaveFinal = this.createFinal(selecciones.get(i),selecciones.get(i+1));
			seleccionService.setSeleccionFinal(selecciones.get(i), llaveFinal);
			seleccionService.setSeleccionFinal(selecciones.get(i+1), llaveFinal);
		}
		
	}

	public Map<String, String> simularFinal() {
		
		Map<String, String> resultado = new HashMap<>();
		
		List<Finale> finals = finalRepository.findAll();
		
		Finale llaveFinal = finals.get(0);
		
		Seleccion seleccion1 = llaveFinal.getSelecciones().get(0);
		Seleccion seleccion2 = llaveFinal.getSelecciones().get(1);
		Partido partidoJugado = seleccionService.jugarPartido(seleccion1.getId(), seleccion2.getId());
		
		llaveFinal.setResultado(partidoJugado.getResultado());
		finalRepository.save(llaveFinal);
		
		resultado.put(seleccion1.getPais() +"-"+ seleccion2.getPais() +": ", partidoJugado.getResultado());
		return resultado;
	}

}
