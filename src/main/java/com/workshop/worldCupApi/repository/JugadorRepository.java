package com.workshop.worldCupApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.worldCupApi.entity.Jugador;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long>{

}
