package com.workshop.worldCupApi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.workshop.worldCupApi.exceptionss.ForbiddenException;
import com.workshop.worldCupApi.exceptionss.NotFoundException;
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
	static final String COLOR_CAMISETA_UNDEFINED = "No se";
	static final String COLOR_CAMISETA_BLANCA = "Blanca";
	static final String COLOR_CAMISETA_AMARILLA = "Amarilla";
	
	public Iterable<Seleccion> getSelecciones() {
		return seleccionRepository.findAll();
	}
	
	public Seleccion getSeleccion(Long seleccionId) throws NotFoundException{
		return seleccionRepository.findById(seleccionId).orElseThrow(()->new  NotFoundException("No se encontró la seleccion con id: " + seleccionId));
	}
	
	public List<Seleccion> getByName(String pais) {
		return seleccionRepository.findByPais(pais);
	}
	
	public Standing getSeleccionStanding(Long seleccionId) throws NotFoundException{
		Seleccion seleccion = this.getSeleccion(seleccionId);
		return seleccion.getStanding();
	}

	protected Seleccion createSeleccion(Seleccion seleccion) {
		
		// crea y setea un standing
		Standing newStanding = standingService.createStanding();
		newStanding.setSeleccion(seleccion);
		seleccion.setStanding(newStanding);
		
		return seleccionRepository.save(seleccion);
	}
	
	public Seleccion createSeleccionConJugadores(Seleccion seleccion) throws ForbiddenException{
		
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
			return savedSeleccion;
		} else {
			// devuelve un error, los jugadores deben ser entre 11 y 26
			throw new ForbiddenException("Error al crear la seleccion"+ seleccion.getPais() +", la seleccion debe tener entre 11 y 26 jugadores"); 
		}
	}
	
	// retorna 204 no content si puede eliminarlo, si falla devuelve 404 not found
	public void deleteSeleccion(Long seleccionId) throws ForbiddenException{
		
		// si no encuentra seleccion con seleccionId arroja excepcion
		Seleccion seleccion = this.getSeleccion(seleccionId);
		
		// detach standing de seleccion
		this.detachStanding(seleccion);
		
		// detach jugadores de seleccion
		this.detachJugadores(seleccion);
		
		// detach partidos de seleccion
		this.detachPartido(seleccion);
		
		try {
			seleccionRepository.deleteById(seleccionId);
		} catch (Exception e) {
			throw new ForbiddenException("Hubo un problema al intentar eliminar la seleccion con id: " + seleccionId);
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
	public boolean addJugadorSeleccion(Long seleccionId, Long jugadorId) throws ForbiddenException{
		// si no encuentra seleccion con seleccionId arroja excepcion
		Seleccion seleccion = this.getSeleccion(seleccionId);
		// se fija que no se pase del limite de jugadores por seleccion
		if (seleccion.getJugadores().size() < 26) {
			// arroja excepcion si no encuentra al jugador
			Jugador jugador = jugadorService.getJugador(jugadorId);
			//setea la seleccion al jugador
			jugadorService.setSeleccionJugador(jugador,seleccion);
			return true;
		}else {
			// devuelve un error de que no se puede mas de 26 jugadores
			throw new ForbiddenException("La seleccion debe tener como maximo 26 jugadores");
		}
	}
	
	// eliminar la relacion de la seleccion con id seleccionId con el jugador con id jugadorId
	public boolean deleteJugadorSeleccion(Long seleccionId, Long jugadorId) throws ForbiddenException{
		// si no encuentra seleccion con seleccionId arroja excepcion
		Seleccion seleccion = this.getSeleccion(seleccionId);
		// se fija que no se pase del limite minimo de jugadores por seleccion
		if (seleccion.getJugadores().size() > 11) {
			// arroja excepcion si no encuentra al jugador
			Jugador jugador = jugadorService.getJugador(jugadorId);
			//setea la seleccion al jugador
			jugadorService.unsetSeleccionJugador(jugador);
			return true;
		}else {
			// devuelve un error de que no se puede menos de 11 jugadores
			throw new ForbiddenException("No se puede eliminar al jugador de la seleccion, al menos debe tener 11 jugadores");
		}
	}
	
	// setea grupo a seleccion
	public boolean setSeleccionGroup(Seleccion seleccion, Group group) {
		
		seleccion.setGroup(group);
		
		try {
			seleccionRepository.save(seleccion);
			return true;
		} catch (Exception e) {
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
			return false;
		}
	}
	
	public boolean setSeleccionSemiFinal(Seleccion seleccion, SemiFinal semiFinal) {
		seleccion.setSemiFinal(semiFinal);
		
		try {
			seleccionRepository.save(seleccion);
			return true;
		} catch (Exception e) {
			return false;
		}	
	}
	
	public boolean setSeleccionFinal(Seleccion seleccion, Finale finale) {
		seleccion.setFinale(finale);
		
		try {
			seleccionRepository.save(seleccion);
			return true;
		} catch (Exception e) {
			return false;
		}	
	}
	
	public List<Seleccion> generate32Selecciones() throws ForbiddenException{
		
		List<Seleccion> selecciones = new ArrayList<>();
		
		// crea la lista de jugadores de Qatar
		Set<Jugador> jugadoresQatar = this.generateJugadores("Qatar");
		// crea la seleccion con la lista de jugadores
		Seleccion qatar = new Seleccion("Qatar", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresQatar);
		this.createSeleccionConJugadores(qatar);
		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(qatar);
		
		
		// crea la lista de jugadores de Ecuador
		Set<Jugador> jugadoresEcuador = this.generateJugadores("Ecuador");		
		// crea la seleccion con la lista de jugadores
		Seleccion ecuador = new Seleccion("Ecuador", COLOR_CAMISETA_AMARILLA, COLOR_CAMISETA_UNDEFINED, jugadoresEcuador);		
		this.createSeleccionConJugadores(ecuador);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(ecuador);
		
		
		// crea la lista de jugadores de Senegal		
		Set<Jugador> jugadoresSenegal = this.generateJugadores("Senegal");		
		// crea la seleccion con la lista de jugadores
		Seleccion senegal = new Seleccion("Senegal", COLOR_CAMISETA_BLANCA, COLOR_CAMISETA_UNDEFINED, jugadoresSenegal);		
		this.createSeleccionConJugadores(senegal);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(senegal);
		
		
		// crea la lista de jugadores de Netherlands		
		Set<Jugador> jugadoresNetherlands = this.generateJugadores("Netherlands");		
		// crea la seleccion con la lista de jugadores
		Seleccion netherlands = new Seleccion("Netherlands", "Naranja", COLOR_CAMISETA_UNDEFINED, jugadoresNetherlands);		
		this.createSeleccionConJugadores(netherlands);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(netherlands);
		
		
		// crea la lista de jugadores de England		
		Set<Jugador> jugadoresEngland = this.generateJugadores("England");		
		// crea la seleccion con la lista de jugadores
		Seleccion england = new Seleccion("England", COLOR_CAMISETA_BLANCA, COLOR_CAMISETA_UNDEFINED, jugadoresEngland);		
		this.createSeleccionConJugadores(england);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(england);
		
		
		// crea la lista de jugadores de USA		
		Set<Jugador> jugadoresUSA = this.generateJugadores("USA");		
		// crea la seleccion con la lista de jugadores
		Seleccion usa = new Seleccion("USA", COLOR_CAMISETA_BLANCA, COLOR_CAMISETA_UNDEFINED, jugadoresUSA);		
		this.createSeleccionConJugadores(usa);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(usa);
		
		
		// crea la lista de jugadores de Iran		
		Set<Jugador> jugadoresIran = this.generateJugadores("Iran");		
		// crea la seleccion con la lista de jugadores
		Seleccion iran = new Seleccion("Iran", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresIran);		
		this.createSeleccionConJugadores(iran);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(iran);
		
		
		// crea la lista de jugadores de Wales		
		Set<Jugador> jugadoresWales = this.generateJugadores("Wales");		
		// crea la seleccion con la lista de jugadores
		Seleccion wales = new Seleccion("Wales", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresWales);		
		this.createSeleccionConJugadores(wales);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(wales);
		
		
		// crea la lista de jugadores de Argentina		
		Set<Jugador> jugadoresArgentina = this.generateJugadores("Argentina");		
		// crea la seleccion con la lista de jugadores
		Seleccion argentina = new Seleccion("Argentina", "Celeste y blanca", "violeta", jugadoresArgentina);		
		this.createSeleccionConJugadores(argentina);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(argentina);
		
		
		// crea la lista de jugadores de Mexico		
		Set<Jugador> jugadoresMexico = this.generateJugadores("Mexico");		
		// crea la seleccion con la lista de jugadores
		Seleccion mexico = new Seleccion("Mexico", "Verde", COLOR_CAMISETA_UNDEFINED, jugadoresMexico);		
		this.createSeleccionConJugadores(mexico);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(mexico);
		
		
		// crea la lista de jugadores de Poland		
		Set<Jugador> jugadoresPoland = this.generateJugadores("Poland");		
		// crea la seleccion con la lista de jugadores
		Seleccion poland = new Seleccion("Poland", COLOR_CAMISETA_BLANCA, COLOR_CAMISETA_UNDEFINED, jugadoresPoland);		
		this.createSeleccionConJugadores(poland);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(poland);
		
		
		// crea la lista de jugadores de Arabia Saudi		
		Set<Jugador> jugadoresSaudiArabia = this.generateJugadores("Saudi Arabia");		
		// crea la seleccion con la lista de jugadores
		Seleccion saudiArabia = new Seleccion("Saudi Arabia", COLOR_CAMISETA_BLANCA, COLOR_CAMISETA_UNDEFINED, jugadoresSaudiArabia);		
		this.createSeleccionConJugadores(saudiArabia);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(saudiArabia);
		
		
		// crea la lista de jugadores de France		
		Set<Jugador> jugadoresFrance = this.generateJugadores("France");		
		// crea la seleccion con la lista de jugadores
		Seleccion france = new Seleccion("France", "Azul", COLOR_CAMISETA_UNDEFINED, jugadoresFrance);		
		this.createSeleccionConJugadores(france);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(france);
		
		
		// crea la lista de jugadores de Denmark		
		Set<Jugador> jugadoresDenmark = this.generateJugadores("Denmark");		
		// crea la seleccion con la lista de jugadores
		Seleccion denmark = new Seleccion("Denmark", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresDenmark);		
		this.createSeleccionConJugadores(denmark);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(denmark);
		
		
		// crea la lista de jugadores de Tunisia		
		Set<Jugador> jugadoresTunisia = this.generateJugadores("Tunisia");		
		// crea la seleccion con la lista de jugadores
		Seleccion tunisia = new Seleccion("Tunisia", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresTunisia);		
		this.createSeleccionConJugadores(tunisia);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(tunisia);
		
		
		// crea la lista de jugadores de Australia		
		Set<Jugador> jugadoresAustralia = this.generateJugadores("Australia");		
		// crea la seleccion con la lista de jugadores
		Seleccion australia = new Seleccion("Australia", COLOR_CAMISETA_AMARILLA, COLOR_CAMISETA_UNDEFINED, jugadoresAustralia);		
		this.createSeleccionConJugadores(australia);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(australia);
		
		
		// crea la lista de jugadores de Spain		
		Set<Jugador> jugadoresSpain = this.generateJugadores("Spain");		
		// crea la seleccion con la lista de jugadores
		Seleccion spain = new Seleccion("Spain", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresSpain);		
		this.createSeleccionConJugadores(spain);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(spain);
		
		
		// crea la lista de jugadores de Germany		
		Set<Jugador> jugadoresGermany = this.generateJugadores("Germany");		
		// crea la seleccion con la lista de jugadores
		Seleccion germany = new Seleccion("Germany", "Blanca y negra", COLOR_CAMISETA_UNDEFINED, jugadoresGermany);		
		this.createSeleccionConJugadores(germany);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(germany);
		
		
		// crea la lista de jugadores de Japan		
		Set<Jugador> jugadoresJapan = this.generateJugadores("Japan");		
		// crea la seleccion con la lista de jugadores
		Seleccion japan = new Seleccion("Japan", "Azul", COLOR_CAMISETA_UNDEFINED, jugadoresJapan);		
		this.createSeleccionConJugadores(japan);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(japan);
		
		
		// crea la lista de jugadores de Costa Rica		
		Set<Jugador> jugadoresCostaRica = this.generateJugadores("Costa Rica");		
		// crea la seleccion con la lista de jugadores
		Seleccion costaRica = new Seleccion("Costa Rica", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresCostaRica);		
		this.createSeleccionConJugadores(costaRica);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(costaRica);
		
		
		// crea la lista de jugadores de Belgium
		Set<Jugador> jugadoresBelgium = this.generateJugadores("Belgium");		
		// crea la seleccion con la lista de jugadores
		Seleccion belgium = new Seleccion("Belgium", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresBelgium);		
		this.createSeleccionConJugadores(belgium);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(belgium);
				
		
		// crea la lista de jugadores de Croatia
		Set<Jugador> jugadoresCroatia = this.generateJugadores("Croatia");		
		// crea la seleccion con la lista de jugadores
		Seleccion croatia = new Seleccion("Croatia", "Blanca con rojo", COLOR_CAMISETA_UNDEFINED, jugadoresCroatia);		
		this.createSeleccionConJugadores(croatia);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(croatia);
		
		
		// crea la lista de jugadores de Moroco
		Set<Jugador> jugadoresMoroco = this.generateJugadores("Moroco");		
		// crea la seleccion con la lista de jugadores
		Seleccion moroco = new Seleccion("Moroco", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresMoroco);		
		this.createSeleccionConJugadores(moroco);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(moroco);
		
		
		// crea la lista de jugadores de Canada
		Set<Jugador> jugadoresCanada = this.generateJugadores("Canada");		
		// crea la seleccion con la lista de jugadores
		Seleccion canada = new Seleccion("Canada", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresCanada);		
		this.createSeleccionConJugadores(canada);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(canada);
		
		
		// crea la lista de jugadores de Brazil
		Set<Jugador> jugadoresBrazil = this.generateJugadores("Brazil");		
		// crea la seleccion con la lista de jugadores
		Seleccion brazil = new Seleccion("Brazil", COLOR_CAMISETA_AMARILLA, COLOR_CAMISETA_UNDEFINED, jugadoresBrazil);		
		this.createSeleccionConJugadores(brazil);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(brazil);
		
		
		// crea la lista de jugadores de Switzerland
		Set<Jugador> jugadoresSwitzerland = this.generateJugadores("Switzerland");		
		// crea la seleccion con la lista de jugadores
		Seleccion switzerland = new Seleccion("Switzerland", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresSwitzerland);		
		this.createSeleccionConJugadores(switzerland);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(switzerland);
		
		
		// crea la lista de jugadores de Serbia
		Set<Jugador> jugadoresSerbia = this.generateJugadores("Serbia");		
		// crea la seleccion con la lista de jugadores
		Seleccion serbia = new Seleccion("Serbia", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresSerbia);		
		this.createSeleccionConJugadores(serbia);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(serbia);
		
		
		// crea la lista de jugadores de Cameroon
		Set<Jugador> jugadoresCameroon = this.generateJugadores("Cameroon");		
		// crea la seleccion con la lista de jugadores
		Seleccion cameroon = new Seleccion("Cameroon", "Verde", COLOR_CAMISETA_UNDEFINED, jugadoresCameroon);		
		this.createSeleccionConJugadores(cameroon);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(cameroon);
		
		
		// crea la lista de jugadores de Portugal
		Set<Jugador> jugadoresPortugal = this.generateJugadores("Portugal");		
		// crea la seleccion con la lista de jugadores
		Seleccion portugal = new Seleccion("Portugal", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresPortugal);		
		this.createSeleccionConJugadores(portugal);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(portugal);
		
		
		// crea la lista de jugadores de Uruguay
		Set<Jugador> jugadoresUruguay = this.generateJugadores("Uruguay");		
		// crea la seleccion con la lista de jugadores
		Seleccion uruguay = new Seleccion("Uruguay", "Celeste", COLOR_CAMISETA_UNDEFINED, jugadoresUruguay);		
		this.createSeleccionConJugadores(uruguay);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(uruguay);
		
		
		// crea la lista de jugadores de Korea
		Set<Jugador> jugadoresKorea = this.generateJugadores("Korea");		
		// crea la seleccion con la lista de jugadores
		Seleccion korea = new Seleccion("Korea", "Roja", COLOR_CAMISETA_UNDEFINED, jugadoresKorea);		
		this.createSeleccionConJugadores(korea);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(korea);
		
		
		// crea la lista de jugadores de Ghana
		Set<Jugador> jugadoresGhana = this.generateJugadores("Ghana");		
		// crea la seleccion con la lista de jugadores
		Seleccion ghana = new Seleccion("Ghana", "blanca", COLOR_CAMISETA_UNDEFINED, jugadoresGhana);		
		this.createSeleccionConJugadores(ghana);		
		// agrega la seleccion a la lista de selecciones
		selecciones.add(ghana);
		
		
		return selecciones;
	}
	
	// generate jugadores set
	public Set<Jugador> generateJugadores(String pais){
		Set<Jugador> jugadores= new HashSet<>();
		for (int i=1; i < 12; i ++) {
			Jugador jugador = new Jugador("Jugador"+i+pais,"apellido"+i+pais,Integer.valueOf(25),Float.valueOf(75),Float.valueOf("1.77"),"Posicion",pais);
			jugadores.add(jugador);
		}
		return jugadores;
	}
	
	public Partido jugarPartido(Long seleccion1Id, Long seleccion2Id) throws ForbiddenException{
		
		if (seleccion1Id.equals(seleccion2Id)) {
			throw new ForbiddenException("Los ids no pueden ser iguales");
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
		
		// genera 2 numeros random
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		Integer cantGoles1 = ThreadLocalRandom.current().nextInt(0, 10 + 1);
		Integer cantGoles2 = ThreadLocalRandom.current().nextInt(0, 10 + 1);
	
		String res; 
		
		// calcula el resultado dependiendo de los valores random y actualiza standings
		if (cantGoles1 < cantGoles2) {
			res = seleccion2.getPais() + " " + cantGoles2.toString() + "-" + cantGoles1.toString();
			standingS2.incrementGanados();
			standingS2.incrementPuntos(3);
			standingS1.incrementPerdidos();
		} else if (cantGoles1.equals(cantGoles2)) {
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
	
		// crea el partido con todos sus atributos
		Partido nuevoPartido = new Partido(seleccion1,seleccion2,cantGoles1,cantGoles2,res);
		
		// Guarda el partido
		partidoService.createPartido(nuevoPartido);
		
		return nuevoPartido;
		
	}

	// para endpoint jugar partido
	public Map<String, String> jugarPartidoEndpoint(Long seleccion1Id, Long seleccion2Id) throws ForbiddenException{
		
		if (seleccion1Id.equals(seleccion2Id)) {
			throw new ForbiddenException("Los ids no pueden ser iguales");
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
		
		// genera 2 numeros random
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		Integer cantGoles1 = ThreadLocalRandom.current().nextInt(0, 10 + 1);
		Integer cantGoles2 = ThreadLocalRandom.current().nextInt(0, 10 + 1);
			
		// inicializa un hash de resultado
		Map<String, String> resultado = new HashMap<>();
		String res; 
		
		// calcula el resultado dependiendo de los valores random y actualiza standings
		if (cantGoles1 < cantGoles2) {
			res = seleccion2.getPais() + " " + cantGoles2.toString() + "-" + cantGoles1.toString();
			standingS2.incrementGanados();
			standingS2.incrementPuntos(3);
			standingS1.incrementPerdidos();
		} else if (cantGoles1.equals(cantGoles2)) {
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
		 
		// setea los demas valores de retorno
		resultado.put("seleccion1", seleccion1.getPais());
		resultado.put("goles " + seleccion1.getPais(), Integer.toString(cantGoles1));
		resultado.put("seleccion2", seleccion2.getPais());
		resultado.put("goles " + seleccion2.getPais(), Integer.toString(cantGoles2));
		
		// crea el partido con todos sus atributos
		Partido nuevoPartido = new Partido(seleccion1,seleccion2,cantGoles1,cantGoles2,res);
		
		// crea el partido
		partidoService.createPartido(nuevoPartido);
		
		return resultado;
		
	}

}
