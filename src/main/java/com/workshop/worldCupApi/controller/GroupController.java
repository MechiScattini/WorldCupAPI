package com.workshop.worldCupApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	/*
	@GetMapping("/{id}")
	public ResponseEntity<Group> getGroupById(@PathVariable("id") Long groupId){
		return groupService.getGroup(groupId);
	}
	*/
	
	@GetMapping("/{letra}")
	public ResponseEntity<?> getGroupByLetra(@PathVariable("letra") String letra){
		return groupService.getGroupByLetra(letra);
	}
	
	@PostMapping()
	public Iterable<Group> generateGroups(){
		return groupService.generateGroups();
	}
	
	// simular partidos de grupos
	@GetMapping("/simularFaseGrupos")
	private ResponseEntity<?> simularFaseGrupos() {
		return groupService.simularFaseGrupos();
	}
	
	// simular partidos de grupos
	@GetMapping("/results")
	private ResponseEntity<?> getAllResultadosFaseGrupos() {
		return groupService.getAllResultadosFaseGrupos();
	}
	
}
