package com.workshop.worldCupApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.worldCupApi.entity.SemiFinal;

@Repository
public interface SemiFinalRepository extends JpaRepository<SemiFinal, Long>{

}
