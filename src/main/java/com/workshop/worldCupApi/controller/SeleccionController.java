package com.workshop.worldCupApi.controller;

import java.util.List;
import java.util.Map;

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
	public ResponseEntity<Seleccion> showSeleccion(@PathVariable("id") Long seleccionId){
		Seleccion seleccion = seleccionService.getSeleccion(seleccionId);
		return ResponseEntity.ok().body(seleccion);	
	}
	
	// getSeleccionStanding endpoint
	@GetMapping("/{id}/standing")
	public ResponseEntity<Standing> getSeleccionStanding(@PathVariable("id") Long seleccionId){
		Standing standing = seleccionService.getSeleccionStanding(seleccionId);
		return ResponseEntity.ok().body(standing);
	}
	
	// create seleccion endpoint
	@PostMapping("/list")
	public ResponseEntity<Seleccion> createSeleccion(@RequestBody Seleccion seleccion){
		Seleccion nuevaSeleccion = seleccionService.createSeleccionConJugadores(seleccion);
		return ResponseEntity.ok().body(nuevaSeleccion);
	}
	
	// generate 32 seleccion endpoint
	@PostMapping()
	public ResponseEntity<List<Seleccion>> generate32Selecciones(){
		List<Seleccion> selecciones = seleccionService.generate32Selecciones();
		return ResponseEntity.ok().body(selecciones);
	}
	
	// delete seleccion endpoint
	@DeleteMapping("/{id}")
	public ResponseEntity<?> destroySeleccion(@PathVariable("id") Long seleccionId){
		seleccionService.deleteSeleccion(seleccionId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	// relaciona la seleccion con id seleccionId con el jugador con id jugadorId
	@PostMapping("/{seleccionId}/{jugadorId}")
	public ResponseEntity<String> addJugadorSeleccion(@PathVariable(value = "seleccionId") Long seleccionId,
			@PathVariable(value = "jugadorId") Long jugadorId){
		if(seleccionService.addJugadorSeleccion(seleccionId,jugadorId)) {
			return ResponseEntity.ok().body("Se agreg?? el jugador con id: "+ jugadorId +" a la seleccion con id: "+ seleccionId +" correctamente");
		}
		return new ResponseEntity<>("Ocurri?? un error agregando el jugador a la seleccion", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	// eliminar la relacion de la seleccion con id seleccionId con el jugador con id jugadorId
	@DeleteMapping("/{seleccionId}/{jugadorId}")
	public ResponseEntity<String> deleteJugadorSeleccion(@PathVariable(value = "seleccionId") Long seleccionId,
			@PathVariable(value = "jugadorId") Long jugadorId){
		if(seleccionService.deleteJugadorSeleccion(seleccionId,jugadorId)) {
			return ResponseEntity.ok().body("Se elimin?? el jugador con id: "+ jugadorId +" de la seleccion con id: "+ seleccionId +" correctamente");
		}
		return new ResponseEntity<>("Ocurri?? un error eliminando el jugador a la seleccion", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/jugarPartido/{seleccion1id}/{seleccion2id}")
	public ResponseEntity<Map<String, String>> jugarPartido(@PathVariable("seleccion1id") Long seleccion1Id, @PathVariable("seleccion2id") Long seleccion2Id) {
		Map<String, String> resultado = seleccionService.jugarPartidoEndpoint(seleccion1Id, seleccion2Id);
		return ResponseEntity.status(HttpStatus.OK).body(resultado);
	}
	
}
