package com.workshop.worldCupApi.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

//import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.worldCupApi.entity.Seleccion;
import com.workshop.worldCupApi.entity.Standing;
import com.workshop.worldCupApi.service.SeleccionService;

@Validated
@RestController
@RequestMapping("/selecciones")
public class SeleccionController {
	
	@Autowired
	private SeleccionService seleccionService;
	
	// indexSelecciones endpoint
	@GetMapping()
	public Iterable<Seleccion> indexSelecciones(){
		return seleccionService.getSelecciones();
	}
	
	// showSeleccion endpoint
	@GetMapping("/{id}")
	public ResponseEntity<?> showSeleccion(@PathVariable("id") Long seleccionId){
		try {
			Seleccion seleccion = seleccionService.getSeleccion(seleccionId);
			return ResponseEntity.ok().body(seleccion);			
		}
		catch(EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}	
	}
	
	// getSeleccionStanding endpoint
	@GetMapping("/{id}/standing")
	public ResponseEntity<?> getSeleccionStanding(@PathVariable("id") Long seleccionId){
		try {
			Standing standing = seleccionService.getSeleccionStanding(seleccionId);
			return ResponseEntity.ok().body(standing);
		}
		catch (EntityNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	// create seleccion endpoint
	@PostMapping("/list")
	public ResponseEntity<?> createSeleccion(@RequestBody Seleccion seleccion){
		try {
			Seleccion nuevaSeleccion = seleccionService.createSeleccionConJugadores(seleccion);
			return ResponseEntity.ok().body(nuevaSeleccion);
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	// generate 32 seleccion endpoint
	@PostMapping()
	public ResponseEntity<?> generate32Selecciones(){
		try {
			List<Seleccion> selecciones = seleccionService.generate32Selecciones();
			return ResponseEntity.ok().body(selecciones);
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	// delete seleccion endpoint
	@DeleteMapping("/{id}")
	public ResponseEntity<?> destroySeleccion(@PathVariable("id") Long seleccionId){
		try {
			seleccionService.deleteSeleccion(seleccionId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	// relaciona la seleccion con id seleccionId con el jugador con id jugadorId
	@PostMapping("/{seleccionId}/{jugadorId}")
	public ResponseEntity<?> addJugadorSeleccion(@PathVariable(value = "seleccionId") Long seleccionId,
			@PathVariable(value = "jugadorId") Long jugadorId){
		return seleccionService.addJugadorSeleccion(seleccionId,jugadorId);
	}
	
	// eliminar la relacion de la seleccion con id seleccionId con el jugador con id jugadorId
	@DeleteMapping("/{seleccionId}/{jugadorId}")
	public ResponseEntity<?> deleteJugadorSeleccion(@PathVariable(value = "seleccionId") Long seleccionId,
			@PathVariable(value = "jugadorId") Long jugadorId){
		return seleccionService.deleteJugadorSeleccion(seleccionId,jugadorId);
	}
	
	@PostMapping("/jugarPartido/{seleccion1id}/{seleccion2id}")
	public ResponseEntity<?> jugarPartido(@PathVariable("seleccion1id") Long seleccion1Id, @PathVariable("seleccion2id") Long seleccion2Id) {
		return seleccionService.jugarPartidoEndpoint(seleccion1Id, seleccion2Id);
	}
	
}
