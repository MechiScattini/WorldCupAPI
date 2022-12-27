package com.workshop.worldCupApi.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.worldCupApi.entity.Group;
import com.workshop.worldCupApi.service.GroupService;

@RestController
@RequestMapping("/groups")
public class GroupController {
	
	@Autowired
	private GroupService groupService;
	
	@GetMapping("/{letra}")
	public ResponseEntity<Group> getGroupByLetra(@PathVariable("letra") String letra){
		Group group = groupService.getGroupByLetra(letra);
		return ResponseEntity.ok().body(group);
	}
	
	@PostMapping()
	public Iterable<Group> generateGroups(){
		return groupService.generateGroups();
	}
	
	// simular partidos de grupos
	@GetMapping("/simularFaseGrupos")
	public ResponseEntity<Map<String, ArrayList<String>>> simularFaseGrupos() {
		Map<String, ArrayList<String>> resultado = groupService.simularFaseGrupos();
		return ResponseEntity.status(HttpStatus.OK).body(resultado);
	}
	
	// simular partidos de grupos
	@GetMapping("/results")
	public ResponseEntity<Map<String, ArrayList<String>>> getAllResultadosFaseGrupos() {
		Map<String, ArrayList<String>> resultados = groupService.getAllResultadosFaseGrupos();
		return ResponseEntity.status(HttpStatus.OK).body(resultados);
	}
	
}
