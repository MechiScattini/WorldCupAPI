package com.workshop.worldCupApi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Standing  implements Comparable<Standing>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique=true,nullable = false)
	private Long id;
	
	private Integer jugados;
	private Integer ganados;
	private Integer empatados;
	private Integer perdidos;
	private Integer puntos;
	private Integer golesFavor;
	private Integer golesContra;
	private Integer difGoles;
	
	@OneToOne
	@JoinColumn(name = "seleccion_id")
	@JsonIgnore
	private Seleccion seleccion;
	
	public Standing() {
		this.jugados=0;
		this.ganados=0;
		this.empatados=0;
		this.perdidos=0;
		this.puntos=0;
		this.golesFavor=0;
		this.golesContra=0;
		this.difGoles=0;
	}
	
	
	public Seleccion getSeleccion() {
		return seleccion;
	}
	
	public void incrementJugados() {
		this.jugados++;
	}
	
	public void incrementGanados() {
		this.ganados++;
	}
	
	public void incrementEmpatados() {
		this.empatados++;
	}
	
	public void incrementPerdidos() {
		this.perdidos++;
	}
	
	public void incrementPuntos(Integer cantPuntos) {
		this.puntos += cantPuntos;
	}

	public void setSeleccion(Seleccion seleccion) {
		this.seleccion = seleccion;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getJugados() {
		return jugados;
	}
	
	public Integer getGanados() {
		return ganados;
	}
	
	public Integer getEmpatados() {
		return empatados;
	}
	
	public Integer getPerdidos() {
		return perdidos;
	}
	
	public Integer getPuntos() {
		return puntos;
	}

	
	public Integer getGolesFavor() {
		return golesFavor;
	}


	public void incrementGolesFavor(Integer cantGoles) {
		this.golesFavor += cantGoles;
	}


	public Integer getGolesContra() {
		return golesContra;
	}


	public void incrementGolesContra(Integer golesContra) {
		this.golesContra += golesContra;
	}


	public Integer getDifGoles() {
		return difGoles;
	}


	public void setDifGoles(Integer difGoles) {
		this.difGoles = difGoles;
	}


	@Override
	public String toString() {
		return "[jugados=" + jugados + ", ganados=" + ganados + ", empatados=" + empatados + ", perdidos="
				+ perdidos + ", puntos=" + puntos + ", golesFavor=" + golesFavor + ", golesContra=" + golesContra
				+ ", difGoles=" + difGoles + "]";
	}
	
	@Override
	public int compareTo(Standing o) {
	  Standing otroStanding = o;
	   
	  return this.getPuntos().compareTo(otroStanding.getPuntos());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Standing other = (Standing) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((id == null) ? 0 : id.hashCode());
        return result;
    }
	
	
}
