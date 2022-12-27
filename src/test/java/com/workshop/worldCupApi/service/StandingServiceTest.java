package com.workshop.worldCupApi.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.workshop.worldCupApi.entity.Standing;
import com.workshop.worldCupApi.repository.StandingRepository;

class StandingServiceTest {
	
	@InjectMocks
	private StandingService standingService;
	
	@Mock
	private StandingRepository standingRepository;
	
	@BeforeEach
	public void start() {
		// inicializa los mocks
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void TestCreateStanding() {
		
		Standing standing = new Standing();
		Mockito.when(standingRepository.save(Mockito.any(Standing.class))).thenReturn(standing);
		
		Standing standing2 = standingService.createStanding();
		
		assertNotNull(standing2);
	}
	
	@Test 
	void TestDeleteStandingWithoutError() throws Exception{
		
		Standing standing = new Standing();
		
		standingService.deleteStanding(standing.getId());
		
		Mockito.verify(standingRepository).deleteById(standing.getId());
			
	}
	
	@Test 
	void TestDeleteStandingWithError() throws Exception{
		
		Long idLong = Long.valueOf(23);
		doThrow(new EntityNotFoundException()).when(standingRepository).deleteById(idLong);
		
		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
				() -> standingService.deleteStanding(idLong));
		
		assertTrue(thrown.getMessage().contentEquals("No se encontró el standing con id: " + idLong));
	}

}
