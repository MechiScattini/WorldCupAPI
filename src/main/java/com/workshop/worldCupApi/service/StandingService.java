package com.workshop.worldCupApi.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.worldCupApi.entity.Standing;
import com.workshop.worldCupApi.repository.StandingRepository;

@Service
public class StandingService {

	@Autowired
	StandingRepository standingRepository;
	
	public Standing createStanding() {
		Standing newStanding = new Standing();
		return standingRepository.save(newStanding);
	}
	
	public void deleteStanding(Long standingId) {
		try {
			standingRepository.deleteById(standingId);
		} catch (Exception e) {
			throw new EntityNotFoundException("No se encontró el standing con id: " + standingId);
		}
	}
	
}
