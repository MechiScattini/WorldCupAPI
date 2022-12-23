package com.workshop.worldCupApi.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.workshop.worldCupApi.entity.Jugador;
import com.workshop.worldCupApi.entity.Partido;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.entity.Standing;
import com.workshop.worldCupApi.repository.SeleccionRepository;

public class SeleccionServiceTest {
	
	@Spy @InjectMocks
	private SeleccionService seleccionService;
	
	@Mock
	SeleccionRepository seleccionRepository;
	
	@Mock
	JugadorService jugadorService;
	
	@Mock
	StandingService standingService;
	
	@Mock
	PartidoService partidoService;
	
	@BeforeEach
	public void start() {
		// inicializa los mocks
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void getSeleccionesTest(){
		// Arrange
		Set<Jugador> jugadoresArgentina = new HashSet<Jugador>();
		Seleccion seleccion1 = new Seleccion("Argentina", "celeste", "violeta", jugadoresArgentina);
		Set<Jugador> jugadoresEcuador = new HashSet<Jugador>();
		Seleccion seleccion2 = new Seleccion("Ecuador", "celeste", "violeta", jugadoresEcuador);
		
		List<Seleccion> selecciones = new ArrayList<Seleccion>();
		selecciones.add(seleccion1);
		selecciones.add(seleccion2);
		
		Mockito.when(seleccionRepository.findAll()).thenReturn(selecciones);
	 
		// Act
		Iterable<Seleccion> returned = seleccionService.getSelecciones();
		
		// Assert
		Mockito.verify(seleccionRepository,Mockito.times(1)).findAll();
		Assertions.assertNotNull(returned);
		Assertions.assertEquals(returned, selecciones);
		
	}
	
	@Test
	public void getSeleccionTest() {
		// Arrange
		Set<Jugador> jugadoresArgentina = new HashSet<Jugador>();
		Seleccion seleccion1 = new Seleccion("Argentina", "celeste", "violeta", jugadoresArgentina);
		Mockito.when(seleccionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(seleccion1));
		
		// Act
		Seleccion returned = seleccionService.getSeleccion(Long.valueOf(1));
		
		// Assert
		Mockito.verify(seleccionRepository, Mockito.times(1)).findById(Mockito.anyLong());
		Assertions.assertNotNull(returned);
		Assertions.assertEquals(returned, seleccion1);
		
	}
	
	/*@Test
	public void getSeleccionExceptionTest() {
		Long seleccionId = Long.valueOf(1);
		Mockito.doThrow(new EntityNotFoundException()).when(seleccionRepository).findById(seleccionId);
		
		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
				() -> seleccionService.getSeleccion(seleccionId));
		
		assertTrue(thrown.getMessage().contentEquals("No se encontró la seleccion con id: " + 1));
	}
	*/
	
	@Test 
	public void createSeleccionCon11JugadoresTest() {
		// Arrange
		Set<Jugador> jugadoresArgentina = new HashSet<Jugador>();
		Jugador jugador1 =  new Jugador();
		jugadoresArgentina.add(jugador1);
		Jugador jugador2 =  new Jugador();
		jugadoresArgentina.add(jugador2);
		Jugador jugador3 =  new Jugador();
		jugadoresArgentina.add(jugador3);
		Jugador jugador4 =  new Jugador();
		jugadoresArgentina.add(jugador4);
		Jugador jugador5 =  new Jugador();
		jugadoresArgentina.add(jugador5);
		Jugador jugador6 =  new Jugador();
		jugadoresArgentina.add(jugador6);
		Jugador jugador7 =  new Jugador();
		jugadoresArgentina.add(jugador7);		
		Jugador jugador8 =  new Jugador();
		jugadoresArgentina.add(jugador8);
		Jugador jugador9 =  new Jugador();
		jugadoresArgentina.add(jugador9);
		Jugador jugador10 =  new Jugador();
		jugadoresArgentina.add(jugador10);
		Jugador jugador11 =  new Jugador();
		jugadoresArgentina.add(jugador11);
		
		Seleccion seleccion1 = new Seleccion("Argentina", "celeste", "violeta", jugadoresArgentina);
		 
		Mockito.doReturn(seleccion1).when(seleccionService).createSeleccion(seleccion1);
		
		// Act
		String result = seleccionService.createSeleccionConJugadores(seleccion1);
		
		// Assert
		Assertions.assertEquals(result,"La seleccion: " + seleccion1.getPais() +" fue creada exitosamente");
	}
	
	@Test 
	public void createSeleccionSinJugadoresTest() {
		// Arrange
		Set<Jugador> jugadoresArgentina = new HashSet<Jugador>();
		
		Seleccion seleccion1 = new Seleccion("Argentina", "celeste", "violeta", jugadoresArgentina);
		 
		Mockito.doReturn(seleccion1).when(seleccionService).createSeleccion(seleccion1);
		
		// Act
		String result = seleccionService.createSeleccionConJugadores(seleccion1);
		
		// Assert
		Assertions.assertEquals(result,"Error al crear la seleccion"+ seleccion1.getPais() +", la seleccion debe tener entre 11 y 26 jugadores");
	}
	
	@Test
	public void jugarPartidoExitosoTest() {
		Set<Jugador> jugadores = new HashSet<Jugador>();
		Standing standing1 = new Standing();
		Seleccion seleccion1 = new Seleccion("Argentina", "celeste", "violeta", jugadores);
		seleccion1.setId(1L);
		seleccion1.setStanding(standing1);
		
		Standing standing2 = new Standing();
		Seleccion seleccion2 = new Seleccion("Colombia", "celeste", "violeta", jugadores);
		seleccion2.setId(2L);
		seleccion2.setStanding(standing2);
		
		Mockito.doReturn(seleccion1).when(seleccionService).getSeleccion(seleccion1.getId());
		Mockito.doReturn(seleccion2).when(seleccionService).getSeleccion(seleccion2.getId());
		
		Partido partidoJugado = seleccionService.jugarPartido(seleccion1.getId(), seleccion2.getId());
		
		Assertions.assertNotEquals(partidoJugado.getResultado(), "Los ids no pueden ser iguales");
		Assertions.assertEquals(standing1.getJugados(), 1);
		Assertions.assertEquals(standing2.getJugados(), 1);
	}
	
	@Test
	public void jugarPartidoIdsIgualesTest() {
		Set<Jugador> jugadores = new HashSet<Jugador>();
		Standing standing1 = new Standing();
		Seleccion seleccion1 = new Seleccion("Argentina", "celeste", "violeta", jugadores);
		seleccion1.setId(1L);
		seleccion1.setStanding(standing1);
		
		Standing standing2 = new Standing();
		Seleccion seleccion2 = new Seleccion("Colombia", "celeste", "violeta", jugadores);
		seleccion2.setId(1L);
		seleccion2.setStanding(standing2);
		
		Mockito.doReturn(seleccion1).when(seleccionService).getSeleccion(seleccion1.getId());
		Mockito.doReturn(seleccion2).when(seleccionService).getSeleccion(seleccion2.getId());
		
		Partido partidoJugado = seleccionService.jugarPartido(seleccion1.getId(), seleccion2.getId());
		
		Assertions.assertEquals(partidoJugado.getResultado(), "Los ids no pueden ser iguales");
		Assertions.assertEquals(standing1.getJugados(), 0);
		Assertions.assertEquals(standing2.getJugados(), 0);
	}

}
