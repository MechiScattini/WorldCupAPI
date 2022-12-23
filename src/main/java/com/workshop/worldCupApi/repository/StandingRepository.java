package com.workshop.worldCupApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.worldCupApi.entity.Standing;

@Repository
public interface StandingRepository extends JpaRepository<Standing, Long>{

}
