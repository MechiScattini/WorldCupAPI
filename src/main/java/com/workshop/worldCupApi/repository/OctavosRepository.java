package com.workshop.worldCupApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.worldCupApi.entity.Octavos;

@Repository
public interface OctavosRepository extends JpaRepository<Octavos, Long>{

}
