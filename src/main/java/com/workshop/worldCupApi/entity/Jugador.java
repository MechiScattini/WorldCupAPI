package com.workshop.worldCupApi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
//import javax.validation.constraints.Max;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Positive;
//import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Jugador {
	/** Entidad que representa un jugador */
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique=true,nullable = false)
	private Long id;
	
	//@NotEmpty(message = "Nombre no puede estar vacío")
	//@Size(min = 1, max = 30, message = "Nombre debe tener entre 1 y 30 caracteres")
	private String nombre;
	
	//@NotEmpty(message = "Apellido no puede estar vacío")
	//@Size(min = 1, max = 30, message = "Apellido debe tener entre 1 y 30 caracteres")
	private String apellido;
	
	//@NotNull(message = "Edad no puede estar vacío")
	//@Min(value = 18, message = "Edad no puede ser menor que 18")
	//@Max(value = 36, message = "Edad no puede ser mayor que 36")
	private Integer edad;
	
	//@NotNull(message = "Peso no puede estar vacío")
	//@Positive(message = "Peso debe ser positivo")
	private Float peso;
	
	//@NotNull(message = "Altura no puede estar vacío")
	//@Positive(message = "Altura debe ser positivo")
	private Float altura;
	
	//@NotNull(message = "Posicion no puede estar vacío")
	//@Size(min = 1, max = 50, message = "Posicion debe tener entre 1 y 50 caracteres")
	private String posicion;
	
	//@NotNull(message = "Nacionalidad no puede estar vacío")
	//@Size(min = 1, max = 30, message = "Nacionalidad debe tener entre 1 y 30 caracteres")
	private String nacionalidad;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seleccion_id")
	@JsonIgnore
	private Seleccion seleccion;
	
	public Jugador() {}
	
	

	public Jugador(
			/*@NotEmpty(message = "Nombre no puede estar vacío") @Size(min = 1, max = 30, message = "Nombre debe tener entre 1 y 30 caracteres") */String nombre,
			/*@NotEmpty(message = "Apellido no puede estar vacío") @Size(min = 1, max = 30, message = "Apellido debe tener entre 1 y 30 caracteres")*/ String apellido,
			/*@NotNull(message = "Edad no puede estar vacío") @Min(value = 18, message = "Edad no puede ser menor que 18") @Max(value = 36, message = "Edad no puede ser mayor que 36")*/ Integer edad,
			/*@NotNull(message = "Peso no puede estar vacío") @Positive(message = "Peso debe ser positivo")*/ Float peso,
			/*@NotNull(message = "Altura no puede estar vacío") @Positive(message = "Altura debe ser positivo")*/ Float altura,
			/*@NotNull(message = "Posicion no puede estar vacío") @Size(min = 1, max = 50, message = "Posicion debe tener entre 1 y 50 caracteres")*/ String posicion,
			/*@NotNull(message = "Nacionalidad no puede estar vacío") @Size(min = 1, max = 30, message = "Nacionalidad debe tener entre 1 y 30 caracteres")*/ String nacionalidad
			) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.peso = peso;
		this.altura = altura;
		this.posicion = posicion;
		this.nacionalidad = nacionalidad;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public Float getPeso() {
		return peso;
	}

	public void setPeso(Float peso) {
		this.peso = peso;
	}

	public Float getAltura() {
		return altura;
	}

	public void setAltura(Float altura) {
		this.altura = altura;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public Seleccion getSeleccion() {
		return seleccion;
	}

	public void setSeleccion(Seleccion seleccion) {
		this.seleccion = seleccion;
	}

	@Override
	public String toString() {
		return "Jugador [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", edad=" + edad + ", peso="
				+ peso + ", altura=" + altura + ", posicion=" + posicion + ", nacionalidad=" + nacionalidad
				+ ", seleccion=" + seleccion.getPais() + "]";
	} 
	
}
