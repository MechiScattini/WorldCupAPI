package com.workshop.worldCupApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.worldCupApi.entity.Seleccion;

@Repository
public interface SeleccionRepository extends JpaRepository<Seleccion, Long>{

	List<Seleccion> findByPais(String pais);
}
