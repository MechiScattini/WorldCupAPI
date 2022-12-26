package com.workshop.worldCupApi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Seleccion {

	/** Entidad que representa una seleccion */
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique=true,nullable = false)
	private Long id;
	
	@NotEmpty(message = "Pais no puede estar vacío")
	@Size(min = 1, max = 30, message = "Pais debe tener entre 1 y 30 caracteres")
	@Column(unique=true)
	private String pais;
	
	@NotEmpty(message = "Color de camiseta titular no puede estar vacío")
	@Size(min = 1, max = 30, message = "Color de camiseta titular debe tener entre 1 y 30 caracteres")
	private String colorCamisetaTitular;
	
	@NotNull(message = "Color de camiseta suplente cannot be empty")
	@Size(min = 1, max = 30, message = "Color de camiseta suplente debe tener entre 1 y 30 caracteres")
	private String colorCamisetaSuplente;
	
	@OneToMany(mappedBy="seleccion")
	@Size(min = 11, max = 26, message = "Seleccion debe tener entre 11 y 26 jugadores")
	private Set<Jugador> jugadores = new HashSet<>();
	
	@ManyToMany(mappedBy = "selecciones")
	private Set<Partido> partidos = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	@JsonIgnore
	private Group group;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "octavo_id")
	@JsonIgnore
	private Octavos octavos;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quarter_id")
	@JsonIgnore
	private Quarter quarter;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semi_final_id")
	@JsonIgnore
	private SemiFinal semiFinal;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "final_id")
	@JsonIgnore
	private Finale finale;
	
	@OneToOne
	@JoinColumn(name = "standing_id")
	private Standing standing;
	
	public Seleccion() {}
	
	
	public Seleccion(
			@NotEmpty(message = "Pais no puede estar vacío") @Size(min = 1, max = 30, message = "Pais debe tener entre 1 y 30 caracteres") String pais,
			@NotEmpty(message = "Color de camiseta titular no puede estar vacío") @Size(min = 1, max = 30, message = "Color de camiseta titular debe tener entre 1 y 30 caracteres") String colorCamisetaTitular,
			@NotNull(message = "Color de camiseta suplente cannot be empty") @Size(min = 1, max = 30, message = "Color de camiseta suplente debe tener entre 1 y 30 caracteres") String colorCamisetaSuplente,
			@Size(min = 11, max = 26, message = "Seleccion debe tener entre 11 y 26 jugadores") Set<Jugador> jugadores) {
		super();
		this.pais = pais;
		this.colorCamisetaTitular = colorCamisetaTitular;
		this.colorCamisetaSuplente = colorCamisetaSuplente;
		this.jugadores = jugadores;
	}
	
	
	
	public Finale getFinale() {
		return finale;
	}


	public void setFinale(Finale finale) {
		this.finale = finale;
	}


	public SemiFinal getSemiFinal() {
		return semiFinal;
	}


	public void setSemiFinal(SemiFinal semiFinal) {
		this.semiFinal = semiFinal;
	}


	public Quarter getQuarter() {
		return quarter;
	}


	public void setQuarter(Quarter quarter) {
		this.quarter = quarter;
	}


	public Octavos getOctavos() {
		return octavos;
	}


	public void setOctavos(Octavos octavos) {
		this.octavos = octavos;
	}


	public void setGroup(Group group) {
		this.group = group;
	}

	public Group getGroup() {
		return group;
	}

	public Standing getStanding() {
		return standing;
	}

	public void setStanding(Standing standing) {
		this.standing = standing;
	}


	public Set<Partido> getPartidos() {
		return partidos;
	}


	public void setPartidos(Set<Partido> partidos) {
		this.partidos = partidos;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getColorCamisetaSuplente() {
		return colorCamisetaSuplente;
	}

	public void setColorCamisetaSuplente(String colorCamisetaSuplente) {
		this.colorCamisetaSuplente = colorCamisetaSuplente;
	}

	public String getColorCamisetaTitular() {
		return colorCamisetaTitular;
	}

	public void setColorCamisetaTitular(String colorCamisetaTitular) {
		this.colorCamisetaTitular = colorCamisetaTitular;
	}

	public Set<Jugador> getJugadores() {
		return jugadores;
	}

	public void setJugadores(Set<Jugador> jugadores) {
		this.jugadores = jugadores;
	}
	
	@Override
	public String toString() {
		return "Seleccion [id=" + id + ", pais=" + pais + ", colorCamisetaTitular=" + colorCamisetaTitular
				+ ", colorCamisetaSuplente=" + colorCamisetaSuplente + ", jugadores=" + jugadores + "]";
	}
}
