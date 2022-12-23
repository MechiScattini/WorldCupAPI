package com.workshop.worldCupApi.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.workshop.worldCupApi.entity.Group;
import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.entity.Standing;
import com.workshop.worldCupApi.repository.GroupRepository;

@Service
public class GroupService {
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private SeleccionService seleccionService;
	
	@Autowired
	private OctavosService octavosService;

	public ResponseEntity<Group> getGroup(Long groupId) {
		Group group = groupRepository.findById(groupId).orElseThrow(()->new  EntityNotFoundException("Couldn't find group with id: " + groupId));
		return ResponseEntity.ok().body(group);
	}
	
	public ResponseEntity<?> getGroupByLetra(String letra) {
		Group group = groupRepository.findByLetra(letra);
		if(group == null) {
			return ResponseEntity
		            .status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body("Hubo un problema encontrando al grupo con letra " + letra);
		}
		else {
			return ResponseEntity.ok().body(group);
		}
	}
	
	public ResponseEntity<?> getAllResultadosFaseGrupos(){
		Map<String, ArrayList<String>> resultado = new HashMap<>();
		
		List<Group> groups = groupRepository.findAll();
		for(Group group : groups) {
			Set<Seleccion> seleccionesGroup = group.getSelecciones();
			List<Seleccion> listSeleccionesGroup = new ArrayList<Seleccion>(seleccionesGroup);		
			String key = "Resultados grupo "+ group.getGroup() +": ";		
			resultado = this.formatearResultado(resultado, listSeleccionesGroup, key);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(resultado);
	}
	
	private Group saveGroup(Group group) {
		return groupRepository.save(group);
	}

	public Iterable<Group> generateGroups() {
		
		// crea una lista de grupos
		List<Group> groupList = new ArrayList<Group>();
		
		// crea el grupo A
		Group groupA = new Group("A");		
		// se trae los equipos del grupo A 
		Seleccion qatar = seleccionService.getByName("Qatar").get(0);
		Seleccion ecuador = seleccionService.getByName("Ecuador").get(0);
		Seleccion netherlands = seleccionService.getByName("Netherlands").get(0);
		Seleccion senegal = seleccionService.getByName("Senegal").get(0);		
		// agrega los equipos al grupo
		groupA.addSeleccion(qatar);
		groupA.addSeleccion(ecuador);
		groupA.addSeleccion(netherlands);
		groupA.addSeleccion(senegal);
		// guarda el grupo
		this.saveGroup(groupA);		
		// setea el grupo a cada equipo
		seleccionService.setSeleccionGroup(qatar, groupA);
		seleccionService.setSeleccionGroup(ecuador, groupA);
		seleccionService.setSeleccionGroup(netherlands, groupA);
		seleccionService.setSeleccionGroup(senegal, groupA);		
		// agrega el grupo a la lista de grupos
		groupList.add(groupA);
		
		
		// crea el grupo B
		Group groupB = new Group("B");				
		// se trae los equipos del grupo B 
		Seleccion england = seleccionService.getByName("England").get(0);
		Seleccion iran = seleccionService.getByName("Iran").get(0);
		Seleccion usa = seleccionService.getByName("USA").get(0);
		Seleccion wales = seleccionService.getByName("Wales").get(0);		
		// agrega los equipos al grupo
		groupB.addSeleccion(england);
		groupB.addSeleccion(iran);
		groupB.addSeleccion(usa);
		groupB.addSeleccion(wales);
		// guarda el grupo
		this.saveGroup(groupB);		
		// setea el grupo a cada equipo
		seleccionService.setSeleccionGroup(england, groupB);
		seleccionService.setSeleccionGroup(iran, groupB);
		seleccionService.setSeleccionGroup(usa, groupB);
		seleccionService.setSeleccionGroup(wales, groupB);		
		// agrega el grupo a la lista de grupos
		groupList.add(groupB);
		
		
		// crea el grupo C
		Group groupC = new Group("C");				
		// se trae los equipos del grupo c 
		Seleccion argentina = seleccionService.getByName("Argentina").get(0);
		Seleccion saudiArabia = seleccionService.getByName("Saudi Arabia").get(0);
		Seleccion mexico = seleccionService.getByName("Mexico").get(0);
		Seleccion poland = seleccionService.getByName("Poland").get(0);		
		// agrega los equipos al grupo
		groupC.addSeleccion(argentina);
		groupC.addSeleccion(saudiArabia);
		groupC.addSeleccion(mexico);
		groupC.addSeleccion(poland);
		// guarda el grupo
		this.saveGroup(groupC);		
		// setea el grupo a cada equipo
		seleccionService.setSeleccionGroup(argentina, groupC);
		seleccionService.setSeleccionGroup(saudiArabia, groupC);
		seleccionService.setSeleccionGroup(mexico, groupC);
		seleccionService.setSeleccionGroup(poland, groupC);		
		// agrega el grupo a la lista de grupos
		groupList.add(groupC);
		
		
		// crea el grupo D
		Group groupD = new Group("D");				
		// se trae los equipos del grupo D
		Seleccion france = seleccionService.getByName("France").get(0);
		Seleccion australia = seleccionService.getByName("Australia").get(0);
		Seleccion denmark = seleccionService.getByName("Denmark").get(0);
		Seleccion tunisia = seleccionService.getByName("Tunisia").get(0);		
		// agrega los equipos al grupo
		groupD.addSeleccion(france);
		groupD.addSeleccion(australia);
		groupD.addSeleccion(denmark);
		groupD.addSeleccion(tunisia);
		// guarda el grupo
		this.saveGroup(groupD);		
		// setea el grupo a cada equipo
		seleccionService.setSeleccionGroup(france, groupD);
		seleccionService.setSeleccionGroup(australia, groupD);
		seleccionService.setSeleccionGroup(denmark, groupD);
		seleccionService.setSeleccionGroup(tunisia, groupD);		
		// agrega el grupo a la lista de grupos
		groupList.add(groupD);
		
		
		// crea el grupo E
		Group groupE = new Group("E");				
		// se trae los equipos del grupo E
		Seleccion spain = seleccionService.getByName("Spain").get(0);
		Seleccion costaRica = seleccionService.getByName("Costa Rica").get(0);
		Seleccion germany = seleccionService.getByName("Germany").get(0);
		Seleccion japan = seleccionService.getByName("Japan").get(0);		
		// agrega los equipos al grupo
		groupE.addSeleccion(spain);
		groupE.addSeleccion(costaRica);
		groupE.addSeleccion(germany);
		groupE.addSeleccion(japan);
		// guarda el grupo
		this.saveGroup(groupE);		
		// setea el grupo a cada equipo
		seleccionService.setSeleccionGroup(spain, groupE);
		seleccionService.setSeleccionGroup(costaRica, groupE);
		seleccionService.setSeleccionGroup(germany, groupE);
		seleccionService.setSeleccionGroup(japan, groupE);		
		// agrega el grupo a la lista de grupos
		groupList.add(groupE);
		
		
		// crea el grupo F
		Group groupF = new Group("F");				
		// se trae los equipos del grupo F
		Seleccion belgium = seleccionService.getByName("Belgium").get(0);
		Seleccion canada = seleccionService.getByName("Canada").get(0);
		Seleccion moroco = seleccionService.getByName("Moroco").get(0);
		Seleccion croatia = seleccionService.getByName("Croatia").get(0);		
		// agrega los equipos al grupo
		groupF.addSeleccion(belgium);
		groupF.addSeleccion(canada);
		groupF.addSeleccion(moroco);
		groupF.addSeleccion(croatia);
		// guarda el grupo
		this.saveGroup(groupF);		
		// setea el grupo a cada equipo
		seleccionService.setSeleccionGroup(belgium, groupF);
		seleccionService.setSeleccionGroup(canada, groupF);
		seleccionService.setSeleccionGroup(moroco, groupF);
		seleccionService.setSeleccionGroup(croatia, groupF);		
		// agrega el grupo a la lista de grupos
		groupList.add(groupF);
		
		
		// crea el grupo G
		Group groupG = new Group("G");				
		// se trae los equipos del grupo G
		Seleccion brazil = seleccionService.getByName("Brazil").get(0);
		Seleccion serbia = seleccionService.getByName("Serbia").get(0);
		Seleccion switzerland = seleccionService.getByName("Switzerland").get(0);
		Seleccion cameroon = seleccionService.getByName("Cameroon").get(0);		
		// agrega los equipos al grupo
		groupG.addSeleccion(brazil);
		groupG.addSeleccion(serbia);
		groupG.addSeleccion(switzerland);
		groupG.addSeleccion(cameroon);
		// guarda el grupo
		this.saveGroup(groupG);		
		// setea el grupo a cada equipo
		seleccionService.setSeleccionGroup(brazil, groupG);
		seleccionService.setSeleccionGroup(serbia, groupG);
		seleccionService.setSeleccionGroup(switzerland, groupG);
		seleccionService.setSeleccionGroup(cameroon, groupG);		
		// agrega el grupo a la lista de grupos
		groupList.add(groupG);
		
		
		// crea el grupo H
		Group groupH = new Group("H");				
		// se trae los equipos del grupo H
		Seleccion portugal = seleccionService.getByName("Portugal").get(0);
		Seleccion ghana = seleccionService.getByName("Ghana").get(0);
		Seleccion uruguay = seleccionService.getByName("Uruguay").get(0);
		Seleccion korea = seleccionService.getByName("Korea").get(0);		
		// agrega los equipos al grupo
		groupH.addSeleccion(portugal);
		groupH.addSeleccion(ghana);
		groupH.addSeleccion(uruguay);
		groupH.addSeleccion(korea);
		// guarda el grupo
		this.saveGroup(groupH);		
		// setea el grupo a cada equipo
		seleccionService.setSeleccionGroup(portugal, groupH);
		seleccionService.setSeleccionGroup(ghana, groupH);
		seleccionService.setSeleccionGroup(uruguay, groupH);
		seleccionService.setSeleccionGroup(korea, groupH);		
		// agrega el grupo a la lista de grupos
		groupList.add(groupH);
				
		return groupList;
		
	}
	
	
	
	public ResponseEntity<?> simularFaseGrupos(){
		
		Map<String, ArrayList<String>> resultado = new HashMap<>();
		List<Seleccion> listFaseGruposWinners = new ArrayList<Seleccion>();
		
		List<Group> groups = groupRepository.findAll();
		for(Group group : groups) {
			Set<Seleccion> seleccionesGroup = group.getSelecciones();
			List<Seleccion> listSeleccionesGroup = new ArrayList<Seleccion>(seleccionesGroup);
			// simula las 6 combinaciones de partidos 
			seleccionService.jugarPartido(listSeleccionesGroup.get(0).getId(), listSeleccionesGroup.get(1).getId());
			seleccionService.jugarPartido(listSeleccionesGroup.get(0).getId(), listSeleccionesGroup.get(2).getId());
			seleccionService.jugarPartido(listSeleccionesGroup.get(0).getId(), listSeleccionesGroup.get(3).getId());
			// seleccion 1
			seleccionService.jugarPartido(listSeleccionesGroup.get(1).getId(), listSeleccionesGroup.get(2).getId());
			seleccionService.jugarPartido(listSeleccionesGroup.get(1).getId(), listSeleccionesGroup.get(3).getId());
			// seleccion 2
			seleccionService.jugarPartido(listSeleccionesGroup.get(2).getId(), listSeleccionesGroup.get(3).getId());
			// calcula los ganadores del grupo
			List<Seleccion> ganadoresGroup = this.calcularGanadores(listSeleccionesGroup);
			// agrega los ganadores a la lista de ganadores
			for(Seleccion seleccion : ganadoresGroup) {
				listFaseGruposWinners.add(seleccion);
			}
			// agrega los ganadores al resultado que devuelve
			String key = "Resultados grupo "+ group.getGroup() +": ";
			resultado = this.formatearResultado(resultado, ganadoresGroup, key);
		}
		
		// genera las llaves de los octavos
		octavosService.generarLlaves(listFaseGruposWinners);
		
		return ResponseEntity.status(HttpStatus.OK).body(resultado);
	}
	
	
	
	private List<Seleccion> calcularGanadores(List<Seleccion> seleccionesGrupo){
		
		// crea una lista de selecciones ganadoras
		List<Seleccion> ganadores = new ArrayList<Seleccion>();
		
		// crea una lista con los standings de cada una
		List<Standing> standings = new ArrayList<Standing>();
		for (Seleccion seleccion : seleccionesGrupo) {
			standings.add(seleccion.getStanding());
		}
		
		// comparador de standings, primero por puntos, despues por diferencia de goles
		Comparator<Standing> multipleComparator = Comparator.comparing(Standing::getPuntos).thenComparing(Comparator.comparing(Standing::getDifGoles));
		// crea una lista ordenada por el criterio anterior
		List<Standing> sortedStandings = standings.stream().sorted(multipleComparator).collect(Collectors.toList());
		
		// agrega las selecciones ganadoras al resultado
		ganadores.add(sortedStandings.get(sortedStandings.size()-2).getSeleccion());
		ganadores.add(sortedStandings.get(sortedStandings.size()-1).getSeleccion());
		
		return ganadores;
	}
	
	// recibe un Map de resultados, una lista de selecciones y el tipo de grupo
	private Map<String, ArrayList<String>> formatearResultado(Map<String, ArrayList<String>> resultado, List<Seleccion> selecciones, String key){
		for(Seleccion seleccion : selecciones) {
			ArrayList<String> list;
			if(resultado.containsKey(key)){
			    // if the key has already been used,
			    // we'll just grab the array list and add the value to it
			    list = resultado.get(key);
			    list.add(seleccion.getPais() + ": " + seleccion.getStanding().toString());
			} else {
			    // if the key hasn't been used yet,
			    // we'll create a new ArrayList<String> object, add the value
			    // and put it in the array list with the new key
			    list = new ArrayList<String>();
			    list.add(seleccion.getPais() + ": " + seleccion.getStanding().toString());
			    resultado.put(key, list);
			}	
		}
		return resultado;
	}
}
