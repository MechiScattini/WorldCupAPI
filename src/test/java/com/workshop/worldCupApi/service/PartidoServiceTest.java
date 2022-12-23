package com.workshop.worldCupApi.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.workshop.worldCupApi.entity.Partido;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.repository.PartidoRepository;

public class PartidoServiceTest {
	
	@InjectMocks
	private PartidoService partidoService;
	
	@Mock
	private PartidoRepository partidoRepository;
	
	@BeforeEach
	public void start() {
		// inicializa los mocks
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void TestCreatePartido() {
		// Arrange
		Seleccion seleccion1= new Seleccion();
		seleccion1.setPais("Argentina");
		seleccion1.setColorCamisetaSuplente("sadf");
		seleccion1.setColorCamisetaTitular("sadf");
		
		Seleccion seleccion2= new Seleccion();
		seleccion2.setPais("Argentina");
		seleccion2.setColorCamisetaSuplente("sadf");
		seleccion2.setColorCamisetaTitular("sadf");
		
		Partido partido = new Partido(seleccion1, seleccion2);
		
		Mockito.when(partidoRepository.save(Mockito.any(Partido.class))).thenReturn(partido);
		
		// Act
		partidoService.createPartido(partido);
		
		// Assert
		
		Mockito.verify(partidoRepository, Mockito.times(1)).save(Mockito.any(Partido.class));
	}
	
}
