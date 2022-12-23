package com.workshop.worldCupApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.worldCupApi.entity.Finale;

@Repository
public interface FinaleRepository extends JpaRepository<Finale, Long>{

}
