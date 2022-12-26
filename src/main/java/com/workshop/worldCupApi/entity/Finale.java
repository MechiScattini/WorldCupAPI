package com.workshop.worldCupApi.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;


@Entity
public class Finale {
	
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(unique=true,nullable = false)
		private Long id;
		
		@Size(min = 1, max = 40, message = "Resultado debe tener entre 1 y 40 caracteres")
		private String resultado;
		
		@OneToMany(mappedBy="finale")
		@Size(min=2, max=2, message = "Final must have only 2 selecciones")
		private List<Seleccion> selecciones = new ArrayList<>();
		
		public Finale() {}
		
		public Finale(Seleccion seleccion1, Seleccion seleccion2) {
			this.selecciones.add(seleccion1);
			this.selecciones.add(seleccion2);
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public List<Seleccion> getSelecciones() {
			return selecciones;
		}

		public void addSeleccion(Seleccion seleccion) {
			this.selecciones.add(seleccion);
		}

		public String getResultado() {
			return resultado;
		}

		public void setResultado(String resultado) {
			this.resultado = resultado;
		}
	
}
