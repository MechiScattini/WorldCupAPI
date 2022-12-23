package com.workshop.worldCupApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.worldCupApi.entity.Quarter;

@Repository
public interface QuarterRepository extends JpaRepository<Quarter, Long>{

}
