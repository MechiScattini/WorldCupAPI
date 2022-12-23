package com.workshop.worldCupApi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.worldCupApi.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>{
	
	Group findByLetra(String letra);

}
