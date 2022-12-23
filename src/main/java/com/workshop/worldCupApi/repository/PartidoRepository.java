package com.workshop.worldCupApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.worldCupApi.entity.Partido;

@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long>{


}
