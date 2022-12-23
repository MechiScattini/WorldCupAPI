package com.workshop.worldCupApi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.workshop.worldCupApi.entity.Partido;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.entity.SemiFinal;
import com.workshop.worldCupApi.repository.SemiFinalRepository;

public class SemiFinalServiceTest {
	
	@InjectMocks
	private SemiFinalService semiFinalService;
	
	@Mock 
	SemiFinalRepository semiFinalRepository;
	
	@Mock
	SeleccionService seleccionService;
	
	@Mock
	FinaleService finalService;
	
	@BeforeEach
	public void start() {
		// inicializa los mocks
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void getSemiFinalsTest() {
		List<SemiFinal> semiFinals = new ArrayList<SemiFinal>();
		SemiFinal semi1 = new SemiFinal();
		SemiFinal semi2 = new SemiFinal();
		semiFinals.add(semi1);
		semiFinals.add(semi2);
		Mockito.when(semiFinalRepository.findAll()).thenReturn(semiFinals);
		
		Iterable<SemiFinal> returned = semiFinalService.getSemiFinals();
		
		Mockito.verify(semiFinalRepository, Mockito.times(1)).findAll();
		assertNotNull(returned);
		assertEquals(returned, semiFinals);
	} 
	
	
	// no args case
	// more args case
	@Test 
	public void createSemiFinalTest() {
		Seleccion seleccion1= new Seleccion();
		seleccion1.setPais("Argentina");
		seleccion1.setColorCamisetaSuplente("sadf");
		seleccion1.setColorCamisetaTitular("sadf");
		
		Seleccion seleccion2= new Seleccion();
		seleccion2.setPais("Argentina");
		seleccion2.setColorCamisetaSuplente("sadf");
		seleccion2.setColorCamisetaTitular("sadf");
		
		SemiFinal semiFinal = new SemiFinal(seleccion1, seleccion2);
		Mockito.when(semiFinalRepository.save(Mockito.any(SemiFinal.class))).thenReturn(semiFinal);

		
		SemiFinal newSemiFinal = semiFinalService.createSemiFinal(seleccion1, seleccion2);
		
		
		Mockito.verify(semiFinalRepository, Mockito.times(1)).save(Mockito.any(SemiFinal.class));
		assertEquals(newSemiFinal, semiFinal);
		assertEquals(semiFinal.getSelecciones().get(0), newSemiFinal.getSelecciones().get(0));
		assertEquals(semiFinal.getSelecciones().get(1), newSemiFinal.getSelecciones().get(1));
		
	}
	
	@Test
	public void simularSemiTest() {
		
		// Arrange
		Seleccion seleccion1= new Seleccion();
		seleccion1.setPais("Argentina");
		seleccion1.setColorCamisetaSuplente("sadf");
		seleccion1.setColorCamisetaTitular("sadf");
		
		Seleccion seleccion2= new Seleccion();
		seleccion2.setPais("Mexico");
		seleccion2.setColorCamisetaSuplente("sadf");
		seleccion2.setColorCamisetaTitular("sadf");
		
		Seleccion seleccion3= new Seleccion();
		seleccion3.setPais("Cuba");
		seleccion3.setColorCamisetaSuplente("sadf");
		seleccion3.setColorCamisetaTitular("sadf");
		
		Seleccion seleccion4= new Seleccion();
		seleccion4.setPais("Colombia");
		seleccion4.setColorCamisetaSuplente("sadf");
		seleccion4.setColorCamisetaTitular("sadf");
		List<SemiFinal> semiFinals = new ArrayList<SemiFinal>();
		SemiFinal semi1 = new SemiFinal(seleccion1,seleccion2);
		SemiFinal semi2 = new SemiFinal(seleccion3,seleccion4);
		semiFinals.add(semi1);
		semiFinals.add(semi2);
		Mockito.when(semiFinalRepository.findAll()).thenReturn(semiFinals);
		
		Partido partido1 = new Partido(seleccion1,seleccion2);
		partido1.setResultado("Argentina");
		
		Partido partido2 = new Partido(seleccion3,seleccion4);
		partido2.setResultado("Colombia");
		
		Mockito.when(seleccionService.jugarPartido(seleccion1.getId(), seleccion2.getId())).thenReturn(partido1, partido2);
		
		// Act
		Map<String, String> resultado = semiFinalService.simularSemi();
		
		
		// Assert
		assertThat(resultado).containsValue("Argentina");
		assertThat(resultado).containsValue("Colombia");
		
	}
}
