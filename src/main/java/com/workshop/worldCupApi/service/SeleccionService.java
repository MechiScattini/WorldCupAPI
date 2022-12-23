package com.workshop.worldCupApi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityNotFoundException;
//import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.workshop.worldCupApi.entity.Finale;
import com.workshop.worldCupApi.entity.Group;
import com.workshop.worldCupApi.entity.Jugador;
import com.workshop.worldCupApi.entity.Octavos;
import com.workshop.worldCupApi.entity.Partido;
import com.workshop.worldCupApi.entity.Quarter;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.entity.SemiFinal;
import com.workshop.worldCupApi.entity.Standing;
import com.workshop.worldCupApi.repository.SeleccionRepository;


@Service
public class SeleccionService {
	
	@Autowired
	private SeleccionRepository seleccionRepository;
	
	@Autowired
	private JugadorService jugadorService;
	
	@Autowired
	private StandingService standingService;

	@Autowired
	private PartidoService partidoService;
	
	public Iterable<Seleccion> getSelecciones() {
		return seleccionRepository.findAll();
	}
	
	public Seleccion getSeleccion(Long seleccionId) {
		return seleccionRepository.findById(seleccionId).orElseThrow(()->new  EntityNotFoundException("No se encontró la seleccion con id: "+seleccionId));
	}
	
	public List<Seleccion> getByName(String pais) {
		return seleccionRepository.findByPais(pais);
	}
	
	public ResponseEntity<Standing> getSeleccionStading(Long seleccionId){
		Seleccion seleccion = seleccionRepository.findById(seleccionId).orElseThrow(()->new  EntityNotFoundException("No se encontró la seleccion con id: "+seleccionId));
		return ResponseEntity.ok().body(seleccion.getStanding());
	}

	protected Seleccion createSeleccion(Seleccion seleccion) {
		
		// crea y setea un standing
		Standing newStanding = standingService.createStanding();
		newStanding.setSeleccion(seleccion);
		seleccion.setStanding(newStanding);
		
		return seleccionRepository.save(seleccion);
	}
	
	public String createSeleccionConJugadores(Seleccion seleccion) {
		
		Set<Jugador> jugadores = seleccion.getJugadores(); 
		
		// se fija que se hayan mandado entre 11 y 26 jugadores
		if (jugadores.size()>= 11 && jugadores.size() <=26) {
			// crea la seleccion
			 Seleccion savedSeleccion = this.createSeleccion(seleccion);
			
			// crea los jugadores y relaciona la seleccion a cada jugador
			for (Jugador jugador : jugadores) {
			    	jugadorService.createJugador(jugador);
			    	jugadorService.setSeleccionJugador(jugador, seleccion);
			}
			return "La seleccion: " + savedSeleccion.getPais() +" fue creada exitosamente";
		} else {
			
			// devuelve un error, los jugadores deben ser entre 11 y 26
			return "Error al crear la seleccion"+ seleccion.getPais() +", la seleccion debe tener entre 11 y 26 jugadores";
		}
	}
	
	/*
	public ResponseEntity<?> createSeleccionConIdsJugadores(@Valid Seleccion seleccion, List<Long> idsJugadores) {
		// se fija que se hayan mandado entre 11 y 26 ids 
		if (idsJugadores.size()>= 11 && idsJugadores.size() <=26) {
			// crea la seleccion
			ResponseEntity<?> response = this.createSeleccion(seleccion);
			
			// crea lista vacia
			List<Jugador> jugadores = new ArrayList<Jugador>();
			// agrega los jugadores con los ids a la lista de jugadores
			for (Long idJugador : idsJugadores) {
				//ResponseEntity<Jugador>jugador = JugadorService.getJugador(idJugador);
				
			}
			return response;
		} else {
			
			// devuelve un error, jugadores deben ser entre 11 y 26
			return ResponseEntity
		            .status(HttpStatus.FORBIDDEN)
		            .body("La seleccion debe tener entre 11 y 26 jugadores");
		}
	}*/
	
	// retorna 204 no content si puede eliminarlo, si falla devuelve 404 not found
	public ResponseEntity<Seleccion> deleteSeleccion(Long seleccionId){
		
		// si no encuentra seleccion con seleccionId arroja excepcion
		Seleccion seleccion = seleccionRepository.findById(seleccionId).orElseThrow(()->new  EntityNotFoundException("No se encontró la seleccion con id: "+seleccionId));
		
		// detach standing de seleccion
		this.detachStanding(seleccion);
		
		// detach jugadores de seleccion
		this.detachJugadores(seleccion);
		
		// detach partidos de seleccion
		this.detachPartido(seleccion);
		
		try {
			seleccionRepository.deleteById(seleccionId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			throw new EntityNotFoundException("No se encontró la seleccion con id: " + seleccionId);
		}
	}
	
	// elimina el standing
	private void detachStanding(Seleccion seleccion) {
		
		// trae el standing de la seleccion
		Standing standing = seleccion.getStanding();
		// lo desvincula de la seleccion
		seleccion.setStanding(null);
		// elimina el standing 
		standingService.deleteStanding(standing.getId());
	}
	
	// desvincula los partidos de la seleccion pero no los elimina
	private void detachPartido(Seleccion seleccion) {
		
		Set<Partido> partidos = seleccion.getPartidos();
		// unsetea la seleccion de cada partido y elimina los partidos
		for (Partido partido : partidos) {
			partidoService.unsetSeleccionPartido(partido, seleccion);
			partidoService.deletePartido(partido.getId());
		}
	}
	
	private void detachJugadores(Seleccion seleccion) {
		Set<Jugador> jugadores = seleccion.getJugadores();
		// unsetea la seleccion de cada jugador
		for (Jugador jugador : jugadores) {
			jugadorService.unsetSeleccionJugador(jugador);
		}
	} 
	
	
	// relaciona la seleccion con id seleccionId con el jugador con id jugadorId
	public ResponseEntity<?> addJugadorSeleccion(Long seleccionId, Long jugadorId){
		// si no encuentra seleccion con seleccionId arroja excepcion
		Seleccion seleccion = seleccionRepository.findById(seleccionId).orElseThrow(()->new  EntityNotFoundException("No se encontró la seleccion con id: "+seleccionId));
		// se fija que no se pase del limite de jugadores por seleccion
		if (seleccion.getJugadores().size() < 26) {
			// arroja excepcion si no encuentra al jugador
			ResponseEntity<Jugador> jugador = jugadorService.getJugador(jugadorId);
			//setea la seleccion al jugador
			jugadorService.setSeleccionJugador(jugador.getBody(),seleccion);
		}else {
			// devuelve un error de que no se puede mas de 26 jugadores
			return ResponseEntity
		            .status(HttpStatus.FORBIDDEN)
		            .body("La seleccion debe tener como maximo 26 jugadores");
		}
		return ResponseEntity.ok().build();
	}
	
	// eliminar la relacion de la seleccion con id seleccionId con el jugador con id jugadorId
	public ResponseEntity<?> deleteJugadorSeleccion(Long seleccionId, Long jugadorId){
		// si no encuentra seleccion con seleccionId arroja excepcion
		Seleccion seleccion = seleccionRepository.findById(seleccionId).orElseThrow(()->new  EntityNotFoundException("No se encontró la seleccion con id: "+seleccionId));
		// se fija que no se pase del limite minimo de jugadores por seleccion
		if (seleccion.getJugadores().size() > 11) {
			// arroja excepcion si no encuentra al jugador
			ResponseEntity<Jugador> jugador = jugadorService.getJugador(jugadorId);
			//setea la seleccion al jugador
			jugadorService.unsetSeleccionJugador(jugador.getBody());
		}else {
			// devuelve un error de que no se puede menos de 11 jugadores
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
		            .body("No se puede eliminar al jugador de la seleccion, al menos debe tener 11 jugadores");
		}
		return ResponseEntity.ok().build();
	}
	
	// setea grupo a seleccion
	public boolean setSeleccionGroup(Seleccion seleccion, Group group) {
		
		seleccion.setGroup(group);
		
		try {
			seleccionRepository.save(seleccion);
			return true;
		} catch (Exception e) {
			System.out.print(e);
			return false;
		}
	}
	
	// setea octavos a seleccion
	public boolean setSeleccionOctavos(Seleccion seleccion, Octavos octavos) {
			
		seleccion.setOctavos(octavos);
		
		try {
			seleccionRepository.save(seleccion);
			return true;
		} catch (Exception e) {
			System.out.print(e);
			return false;
		}
	}
	
	// setea octavos a seleccion
	public boolean setSeleccionQuarter(Seleccion seleccion, Quarter quarter) {
			
		seleccion.setQuarter(quarter);
		
		try {
			seleccionRepository.save(seleccion);
			return true;
		} catch (Exception e) {
			System.out.print(e);
			return false;
		}
	}
	
	public boolean setSeleccionSemiFinal(Seleccion seleccion, SemiFinal semiFinal) {
		seleccion.setSemiFinal(semiFinal);
		
		try {
			seleccionRepository.save(seleccion);
			return true;
		} catch (Exception e) {
			System.out.print(e);
			return false;
		}	
	}
	
	public boolean setSeleccionFinal(Seleccion seleccion, Finale finale) {
		seleccion.setFinale(finale);
		
		try {
			seleccionRepository.save(seleccion);
			return true;
		} catch (Exception e) {
			System.out.print(e);
			return false;
		}	
	}
	
	public ResponseEntity<List<Seleccion>> generate32Selecciones(){
		
		List<Seleccion> selecciones = new ArrayList<Seleccion>();
		
		// crea la lista de jugadores de Qatar
		Set<Jugador> jugadoresQatar = this.generateJugadores("Qatar");
		// crea la seleccion con la lista de jugadores
		Seleccion qatar = new Seleccion("Qatar", "Roja", "No se", jugadoresQatar);
		this.createSeleccionConJugadores(qatar);
		// agrega la seleccion a la lista de selecciones
		selecciones.add(qatar);
		
		
		// crea la lista de jugadores de Ecuador
		Set<Jugador> jugadoresEcuador = this.generateJugadores("Ecuador");		
		// crea la seleccion con la lista de jugadores
		Seleccion ecuador = new Seleccion("Ecuador", "Amarilla", "No se", jugadoresEcuador);		
		this.createSeleccionConJugadores(ecuador);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(ecuador);
		
		
		// crea la lista de jugadores de Senegal		
		Set<Jugador> jugadoresSenegal = this.generateJugadores("Senegal");		
		// crea la seleccion con la lista de jugadores
		Seleccion senegal = new Seleccion("Senegal", "Blanca", "No se", jugadoresSenegal);		
		this.createSeleccionConJugadores(senegal);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(senegal);
		
		
		// crea la lista de jugadores de Netherlands		
		Set<Jugador> jugadoresNetherlands = this.generateJugadores("Netherlands");		
		// crea la seleccion con la lista de jugadores
		Seleccion netherlands = new Seleccion("Netherlands", "Naranja", "No se", jugadoresNetherlands);		
		this.createSeleccionConJugadores(netherlands);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(netherlands);
		
		
		// crea la lista de jugadores de England		
		Set<Jugador> jugadoresEngland = this.generateJugadores("England");		
		// crea la seleccion con la lista de jugadores
		Seleccion england = new Seleccion("England", "Blanca", "No se", jugadoresEngland);		
		this.createSeleccionConJugadores(england);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(england);
		
		
		// crea la lista de jugadores de USA		
		Set<Jugador> jugadoresUSA = this.generateJugadores("USA");		
		// crea la seleccion con la lista de jugadores
		Seleccion USA = new Seleccion("USA", "Blanca", "No se", jugadoresUSA);		
		this.createSeleccionConJugadores(USA);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(USA);
		
		
		// crea la lista de jugadores de Iran		
		Set<Jugador> jugadoresIran = this.generateJugadores("Iran");		
		// crea la seleccion con la lista de jugadores
		Seleccion Iran = new Seleccion("Iran", "Roja", "No se", jugadoresIran);		
		this.createSeleccionConJugadores(Iran);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Iran);
		
		
		// crea la lista de jugadores de Wales		
		Set<Jugador> jugadoresWales = this.generateJugadores("Wales");		
		// crea la seleccion con la lista de jugadores
		Seleccion Wales = new Seleccion("Wales", "Roja", "No se", jugadoresWales);		
		this.createSeleccionConJugadores(Wales);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Wales);
		
		
		// crea la lista de jugadores de Argentina		
		Set<Jugador> jugadoresArgentina = this.generateJugadores("Argentina");		
		// crea la seleccion con la lista de jugadores
		Seleccion Argentina = new Seleccion("Argentina", "Celeste y blanca", "violeta", jugadoresArgentina);		
		this.createSeleccionConJugadores(Argentina);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Argentina);
		
		
		// crea la lista de jugadores de Mexico		
		Set<Jugador> jugadoresMexico = this.generateJugadores("Mexico");		
		// crea la seleccion con la lista de jugadores
		Seleccion Mexico = new Seleccion("Mexico", "Verde", "no se", jugadoresMexico);		
		this.createSeleccionConJugadores(Mexico);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Mexico);
		
		
		// crea la lista de jugadores de Poland		
		Set<Jugador> jugadoresPoland = this.generateJugadores("Poland");		
		// crea la seleccion con la lista de jugadores
		Seleccion Poland = new Seleccion("Poland", "Blanca", "no se", jugadoresPoland);		
		this.createSeleccionConJugadores(Poland);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Poland);
		
		
		// crea la lista de jugadores de Arabia Saudi		
		Set<Jugador> jugadoresSaudiArabia = this.generateJugadores("Saudi Arabia");		
		// crea la seleccion con la lista de jugadores
		Seleccion SaudiArabia = new Seleccion("Saudi Arabia", "Blanca", "no se", jugadoresSaudiArabia);		
		this.createSeleccionConJugadores(SaudiArabia);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(SaudiArabia);
		
		
		// crea la lista de jugadores de France		
		Set<Jugador> jugadoresFrance = this.generateJugadores("France");		
		// crea la seleccion con la lista de jugadores
		Seleccion France = new Seleccion("France", "Azul", "no se", jugadoresFrance);		
		this.createSeleccionConJugadores(France);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(France);
		
		
		// crea la lista de jugadores de Denmark		
		Set<Jugador> jugadoresDenmark = this.generateJugadores("Denmark");		
		// crea la seleccion con la lista de jugadores
		Seleccion Denmark = new Seleccion("Denmark", "Roja", "no se", jugadoresDenmark);		
		this.createSeleccionConJugadores(Denmark);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Denmark);
		
		
		// crea la lista de jugadores de Tunisia		
		Set<Jugador> jugadoresTunisia = this.generateJugadores("Tunisia");		
		// crea la seleccion con la lista de jugadores
		Seleccion Tunisia = new Seleccion("Tunisia", "Roja", "no se", jugadoresTunisia);		
		this.createSeleccionConJugadores(Tunisia);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Tunisia);
		
		
		// crea la lista de jugadores de Australia		
		Set<Jugador> jugadoresAustralia = this.generateJugadores("Australia");		
		// crea la seleccion con la lista de jugadores
		Seleccion Australia = new Seleccion("Australia", "Amarilla", "no se", jugadoresAustralia);		
		this.createSeleccionConJugadores(Australia);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Australia);
		
		
		// crea la lista de jugadores de Spain		
		Set<Jugador> jugadoresSpain = this.generateJugadores("Spain");		
		// crea la seleccion con la lista de jugadores
		Seleccion Spain = new Seleccion("Spain", "Roja", "no se", jugadoresSpain);		
		this.createSeleccionConJugadores(Spain);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Spain);
		
		
		// crea la lista de jugadores de Germany		
		Set<Jugador> jugadoresGermany = this.generateJugadores("Germany");		
		// crea la seleccion con la lista de jugadores
		Seleccion Germany = new Seleccion("Germany", "Blanca y negra", "no se", jugadoresGermany);		
		this.createSeleccionConJugadores(Germany);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Germany);
		
		
		// crea la lista de jugadores de Japan		
		Set<Jugador> jugadoresJapan = this.generateJugadores("Japan");		
		// crea la seleccion con la lista de jugadores
		Seleccion Japan = new Seleccion("Japan", "Azul", "no se", jugadoresJapan);		
		this.createSeleccionConJugadores(Japan);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Japan);
		
		
		// crea la lista de jugadores de Costa Rica		
		Set<Jugador> jugadoresCostaRica = this.generateJugadores("Costa Rica");		
		// crea la seleccion con la lista de jugadores
		Seleccion CostaRica = new Seleccion("Costa Rica", "Roja", "no se", jugadoresCostaRica);		
		this.createSeleccionConJugadores(CostaRica);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(CostaRica);
		
		
		// crea la lista de jugadores de Belgium
		Set<Jugador> jugadoresBelgium = this.generateJugadores("Belgium");		
		// crea la seleccion con la lista de jugadores
		Seleccion Belgium = new Seleccion("Belgium", "Roja", "no se", jugadoresBelgium);		
		this.createSeleccionConJugadores(Belgium);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Belgium);
				
		
		// crea la lista de jugadores de Croatia
		Set<Jugador> jugadoresCroatia = this.generateJugadores("Croatia");		
		// crea la seleccion con la lista de jugadores
		Seleccion Croatia = new Seleccion("Croatia", "Blanca con rojo", "no se", jugadoresCroatia);		
		this.createSeleccionConJugadores(Croatia);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Croatia);
		
		
		// crea la lista de jugadores de Moroco
		Set<Jugador> jugadoresMoroco = this.generateJugadores("Moroco");		
		// crea la seleccion con la lista de jugadores
		Seleccion Moroco = new Seleccion("Moroco", "Roja", "no se", jugadoresMoroco);		
		this.createSeleccionConJugadores(Moroco);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Moroco);
		
		
		// crea la lista de jugadores de Canada
		Set<Jugador> jugadoresCanada = this.generateJugadores("Canada");		
		// crea la seleccion con la lista de jugadores
		Seleccion Canada = new Seleccion("Canada", "Roja", "no se", jugadoresCanada);		
		this.createSeleccionConJugadores(Canada);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Canada);
		
		
		// crea la lista de jugadores de Brazil
		Set<Jugador> jugadoresBrazil = this.generateJugadores("Brazil");		
		// crea la seleccion con la lista de jugadores
		Seleccion Brazil = new Seleccion("Brazil", "Amarilla", "no se", jugadoresBrazil);		
		this.createSeleccionConJugadores(Brazil);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Brazil);
		
		
		// crea la lista de jugadores de Switzerland
		Set<Jugador> jugadoresSwitzerland = this.generateJugadores("Switzerland");		
		// crea la seleccion con la lista de jugadores
		Seleccion Switzerland = new Seleccion("Switzerland", "Roja", "no se", jugadoresSwitzerland);		
		this.createSeleccionConJugadores(Switzerland);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Switzerland);
		
		
		// crea la lista de jugadores de Serbia
		Set<Jugador> jugadoresSerbia = this.generateJugadores("Serbia");		
		// crea la seleccion con la lista de jugadores
		Seleccion Serbia = new Seleccion("Serbia", "Roja", "no se", jugadoresSerbia);		
		this.createSeleccionConJugadores(Serbia);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Serbia);
		
		
		// crea la lista de jugadores de Cameroon
		Set<Jugador> jugadoresCameroon = this.generateJugadores("Cameroon");		
		// crea la seleccion con la lista de jugadores
		Seleccion Cameroon = new Seleccion("Cameroon", "Verde", "no se", jugadoresCameroon);		
		this.createSeleccionConJugadores(Cameroon);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Cameroon);
		
		
		// crea la lista de jugadores de Portugal
		Set<Jugador> jugadoresPortugal = this.generateJugadores("Portugal");		
		// crea la seleccion con la lista de jugadores
		Seleccion Portugal = new Seleccion("Portugal", "Roja", "no se", jugadoresPortugal);		
		this.createSeleccionConJugadores(Portugal);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Portugal);
		
		
		// crea la lista de jugadores de Uruguay
		Set<Jugador> jugadoresUruguay = this.generateJugadores("Uruguay");		
		// crea la seleccion con la lista de jugadores
		Seleccion Uruguay = new Seleccion("Uruguay", "Celeste", "no se", jugadoresUruguay);		
		this.createSeleccionConJugadores(Uruguay);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Uruguay);
		
		
		// crea la lista de jugadores de Korea
		Set<Jugador> jugadoresKorea = this.generateJugadores("Korea");		
		// crea la seleccion con la lista de jugadores
		Seleccion Korea = new Seleccion("Korea", "Roja", "no se", jugadoresKorea);		
		this.createSeleccionConJugadores(Korea);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Korea);
		
		
		// crea la lista de jugadores de Ghana
		Set<Jugador> jugadoresGhana = this.generateJugadores("Ghana");		
		// crea la seleccion con la lista de jugadores
		Seleccion Ghana = new Seleccion("Ghana", "blanca", "no se", jugadoresGhana);		
		this.createSeleccionConJugadores(Ghana);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(Ghana);
		
		
		return ResponseEntity.status(HttpStatus.OK).body(selecciones);
	}
	
	// generate jugadores set
	public Set<Jugador> generateJugadores(String pais){
		Set<Jugador> jugadores= new HashSet<Jugador>();
		for (int i=1; i < 12; i ++) {
			Jugador jugador = new Jugador("Jugador"+i+pais,"apellido"+i+pais,Integer.valueOf(25),Float.valueOf(75),Float.valueOf("1.77"),"Posicion",pais);
			jugadores.add(jugador);
		}
		return jugadores;
	}
	
	public Partido jugarPartido(Long seleccion1Id, Long seleccion2Id) {
		
		if (seleccion1Id == seleccion2Id) {
			Partido nuevoPartido = new Partido(null,null);
			nuevoPartido.setResultado("Los ids no pueden ser iguales");
			return nuevoPartido;
		}
		// se fija si las selecciones existen, si alguna no existe tira excepcion
		Seleccion seleccion1 = this.getSeleccion(seleccion1Id);
		Seleccion seleccion2 = this.getSeleccion(seleccion2Id);
		
		// se trae los standings de cada una para actualizarlos
		Standing standingS1 = seleccion1.getStanding();
		Standing standingS2 = seleccion2.getStanding();
		
		// incrementa la cant de partidos jugados de cada una
		standingS1.incrementJugados();
		standingS2.incrementJugados();
		
		// crea el partido con los nombres de ambas selecciones
		Partido nuevoPartido = new Partido(seleccion1,seleccion2);
		
		
		// agrega las selecciones al partido
		nuevoPartido.addSeleccion(seleccion1);
		nuevoPartido.addSeleccion(seleccion2);
		
		// genera 2 numeros random
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		Integer cantGoles1 = ThreadLocalRandom.current().nextInt(0, 10 + 1);
		Integer cantGoles2 = ThreadLocalRandom.current().nextInt(0, 10 + 1);
		
		// setea esos numeros random a la cantGoels del partido
		nuevoPartido.setCantGolesS1(cantGoles1);
		nuevoPartido.setCantGolesS2(cantGoles2);
		
		String res; 
		
		// calcula el resultado dependiendo de los valores random y actualiza standings
		if (cantGoles1 < cantGoles2) {
			res = seleccion2.getPais() + " " + cantGoles2.toString() + "-" + cantGoles1.toString();
			standingS2.incrementGanados();
			standingS2.incrementPuntos(3);
			standingS1.incrementPerdidos();
		} else if (cantGoles1 == cantGoles2) {
			res = "Empate " + cantGoles1.toString() + "-" + cantGoles2.toString();
			standingS1.incrementEmpatados();
			standingS1.incrementPuntos(1);
			standingS2.incrementEmpatados();
			standingS2.incrementPuntos(1);
		} else {
			res = seleccion1.getPais() + " " + cantGoles1.toString() + "-" + cantGoles2.toString();
			standingS1.incrementGanados();
			standingS1.incrementPuntos(3);
			standingS2.incrementPerdidos();
		}
		
		
		// actualiza goles a favor y en contra de cada equipo
		standingS1.incrementGolesFavor(cantGoles1);
		standingS1.incrementGolesContra(cantGoles2);
		
		standingS2.incrementGolesFavor(cantGoles2);
		standingS2.incrementGolesContra(cantGoles1);
		
		// actualiza la diferencia de goles de cada equipo
		standingS1.setDifGoles(standingS1.getGolesFavor()-standingS1.getGolesContra());
		standingS2.setDifGoles(standingS2.getGolesFavor()-standingS2.getGolesContra());
		
		// setea el resultado en el partido 
		nuevoPartido.setResultado(res);
		
		// crea el partido
		partidoService.createPartido(nuevoPartido);
		
		return nuevoPartido;
		
	}

	// para endpoint jugar partido
	public ResponseEntity<?> jugarPartidoEndpoint(Long seleccion1Id, Long seleccion2Id) {
		
		if (seleccion1Id == seleccion2Id) {
			return ResponseEntity
		            .status(HttpStatus.FORBIDDEN)
		            .body("Los ids de las selecciones no pueden ser iguales");
		}
		// se fija si las selecciones existen, si alguna no existe tira excepcion
		Seleccion seleccion1 = this.getSeleccion(seleccion1Id);
		Seleccion seleccion2 = this.getSeleccion(seleccion2Id);
		
		// se trae los standings de cada una para actualizarlos
		Standing standingS1 = seleccion1.getStanding();
		Standing standingS2 = seleccion2.getStanding();
		
		// incrementa la cant de partidos jugados de cada una
		standingS1.incrementJugados();
		standingS2.incrementJugados();
		
		// crea el partido con los nombres de ambas selecciones
		Partido nuevoPartido = new Partido(seleccion1,seleccion2);
		
		
		// agrega las selecciones al partido
		nuevoPartido.addSeleccion(seleccion1);
		nuevoPartido.addSeleccion(seleccion2);
		
		// genera 2 numeros random
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		Integer cantGoles1 = ThreadLocalRandom.current().nextInt(0, 10 + 1);
		Integer cantGoles2 = ThreadLocalRandom.current().nextInt(0, 10 + 1);
		
		// setea esos numeros random a la cantGoels del partido
		nuevoPartido.setCantGolesS1(cantGoles1);
		nuevoPartido.setCantGolesS2(cantGoles2);
		
		// inicializa un hash de resultado
		Map<String, String> resultado = new HashMap<>();
		String res; 
		
		// calcula el resultado dependiendo de los valores random y actualiza standings
		if (cantGoles1 < cantGoles2) {
			res = seleccion2.getPais() + " " + cantGoles2.toString() + "-" + cantGoles1.toString();
			standingS2.incrementGanados();
			standingS2.incrementPuntos(3);
			standingS1.incrementPerdidos();
		} else if (cantGoles1 == cantGoles2) {
			res = "Empate " + cantGoles1.toString() + "-" + cantGoles2.toString();
			standingS1.incrementEmpatados();
			standingS1.incrementPuntos(1);
			standingS2.incrementEmpatados();
			standingS2.incrementPuntos(1);
		} else {
			res = seleccion1.getPais() + " " + cantGoles1.toString() + "-" + cantGoles2.toString();
			standingS1.incrementGanados();
			standingS1.incrementPuntos(3);
			standingS2.incrementPerdidos();
		}
		
		
		// actualiza goles a favor y en contra de cada equipo
		standingS1.incrementGolesFavor(cantGoles1);
		standingS1.incrementGolesContra(cantGoles2);
		
		standingS2.incrementGolesFavor(cantGoles2);
		standingS2.incrementGolesContra(cantGoles1);
		
		// actualiza la diferencia de goles de cada equipo
		standingS1.setDifGoles(standingS1.getGolesFavor()-standingS1.getGolesContra());
		standingS2.setDifGoles(standingS2.getGolesFavor()-standingS2.getGolesContra());
		
		// setea el resultado en el partido y en el resultado que retorna
		resultado.put("resultado", res);
		nuevoPartido.setResultado(res);
		 
		// setea los demas valores de retorno
		resultado.put("seleccion1", seleccion1.getPais());
		resultado.put("goles " + seleccion1.getPais(), Integer.toString(cantGoles1));
		resultado.put("seleccion2", seleccion2.getPais());
		resultado.put("goles " + seleccion2.getPais(), Integer.toString(cantGoles2));
		
		// crea el partido
		partidoService.createPartido(nuevoPartido);
		
		return ResponseEntity.status(HttpStatus.OK).body(resultado);
		
	}

}
