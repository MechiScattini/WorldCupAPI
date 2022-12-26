package com.workshop.worldCupApi.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity
public class Group {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique=true,nullable = false)
	private Long id;
	
	@Size(min = 1, max = 1, message = "Letra must be 1 character")
	private String letra;
	
	@OneToMany(mappedBy="group")
	@Size(min=4, max=4, message = "Group must have only 4 selecciones")
	private Set<Seleccion> selecciones = new HashSet<>();
	
	public Group() {}
	
	public Group(String group) {
		this.letra = group;
	}
	
	public void addSeleccion(Seleccion seleccion) {
		this.selecciones.add(seleccion);
	}
	
	public void removeSeleccion(Seleccion seleccion) {
		this.selecciones.remove(seleccion);
	}
	
	public Set<Seleccion> getSelecciones() {
		return selecciones;
	}

	public Long getId() {
		return id;
	}

	public String getGroup() {
		return letra;
	}

	@Override
	public String toString() {
		StringBuilder selec = new StringBuilder();
		for (Seleccion seleccion : selecciones) {
			selec.append(seleccion.getPais() + ", ");
		}
		return "Group [id=" + id + ", letra=" + letra + ", selecciones=" + selec + "]";
	}
	
	
}
