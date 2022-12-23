package com.workshop.worldCupApi.controller;

//import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
		return seleccionService.getSeleccionStading(seleccionId);
	}
	
	// create seleccion endpoint
	@PostMapping("/list")
	public String createSeleccion(@RequestBody Seleccion seleccion){
		return seleccionService.createSeleccionConJugadores(seleccion);  
	}
	
	// generate 32 seleccion endpoint
	@PostMapping()
	public ResponseEntity<?> generate32Selecciones(){
		return seleccionService.generate32Selecciones();
	}
	
	/*@PostMapping("/ids")
	public ResponseEntity<?> createSeleccionWithIds(@RequestBody @Valid Seleccion seleccion, List<Long> idsJugadores){
		return seleccionService.createSeleccionConIdsJugadores(seleccion, idsJugadores);
	}
	*/
	
	// delete seleccion endpoint
	@DeleteMapping("/{id}")
	public ResponseEntity<Seleccion> destroySeleccion(@PathVariable("id") Long seleccionId){
		return seleccionService.deleteSeleccion(seleccionId);
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
