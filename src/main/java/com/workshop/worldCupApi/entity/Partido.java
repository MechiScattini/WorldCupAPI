package com.workshop.worldCupApi.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Partido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique=true,nullable = false)
	private Long id;
	
	@Min(value = 0, message = "La cantidad de goles no puede ser menor que 0")
	private Integer cantGolesS1;
	
	@Min(value = 0, message = "La cantidad de goles no puede ser menor que 0")
	private Integer cantGolesS2;
	
	@NotNull
	private Date fecha;
	
	@Size(min = 1, max = 40, message = "Resultado debe tener entre 1 y 40 caracteres")
	private String resultado;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name="partido_selecciones",
			joinColumns = @JoinColumn(name = "partido_id"),
			inverseJoinColumns = @JoinColumn(name = "seleccion_id"))
	@Size(min=2, max=2)
	@JsonIgnore
	private Set<Seleccion> selecciones = new HashSet<>(); 
	
	public Partido() {}

	public Partido(Seleccion seleccion1, Seleccion seleccion2, Integer cantGolesS1, Integer cantGolesS2, String resultado) {
		this.selecciones.add(seleccion1);
		this.selecciones.add(seleccion2);
		this.setCantGolesS1(cantGolesS1);
		this.setCantGolesS2(cantGolesS2);
		this.setResultado(resultado);
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

	public void setSelecciones(Set<Seleccion> selecciones) {
		this.selecciones = selecciones;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getCantGolesS1() {
		return cantGolesS1;
	}
	public void setCantGolesS1(Integer cantGolesS1) {
		this.cantGolesS1 = cantGolesS1;
	}
	public Integer getCantGolesS2() {
		return cantGolesS2;
	}
	public void setCantGolesS2(Integer cantGolesS2) {
		this.cantGolesS2 = cantGolesS2;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	
	
	
}
