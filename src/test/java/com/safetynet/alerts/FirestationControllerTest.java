package com.safetynet.alerts;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.FirestationController;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationServiceI;

@WebMvcTest(controllers = FirestationController.class)
class FirestationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirestationServiceI firestationService;

	Firestation firestation;
	List<Firestation> firestations = new ArrayList<>();
	Firestation firestation1 = new Firestation();
	Firestation firestation2 = new Firestation();
	

	@BeforeEach
	private void setUpPerTest() {

		firestation1.setAddress("8 rue du general leclerc");
		firestation1.setStation(1);

		firestation2.setAddress("14 place de la halle");
		firestation2.setStation(2);

		firestations.add(firestation1);
		firestations.add(firestation2);
	}

	@Test
	void testPostFirestation() throws Exception {
		//ARRANGE
		when(firestationService.getFirestation("8 rue du general leclerc")).thenReturn(null);
		when(firestationService.postFirestation(firestation1)).thenReturn(firestation1);
		//ACT AND ASSERT
		mockMvc.perform(post("/firestation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(firestation1)))
				.andExpect(status().isCreated());

		verify(firestationService).postFirestation(firestation1);
	}
	
	@Test
	void testPostFirestationWithInvalidRequest() throws Exception {
		// ARRANGE
		Firestation firestation3 = new Firestation();
		firestation3.setStation(0);
		firestation3.setAddress("");
		
		// ACT AND ASSERT
		mockMvc.perform(post("/firestation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(firestation3)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void testPostFirestationIfAlreadyExist() throws Exception {
		// ARRANGE
		when(firestationService.getFirestation("8 rue du general leclerc")).thenReturn(firestation1);
		//ACT AND ASSERT
				mockMvc.perform(post("/firestation")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(firestation1)))
						.andExpect(status().isConflict());
	}

	@Test
	void testPutFirestation() throws Exception {
		//ARRANGE
		when(firestationService.getFirestation("14 place de la halle")).thenReturn(firestation2);
		when(firestationService.putFirestation(firestation2)).thenReturn(firestation2);
		//ACT AND ASSERT
		mockMvc.perform(put("/firestation").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(firestation2))).andExpect(status().isCreated());
	
		verify(firestationService).putFirestation(firestation2);
	}
	
	@Test
	void testPutFirestationIfNotExist() throws Exception {
		//ARRANGE
		when(firestationService.getFirestation("14 place de la halle")).thenReturn(null);
		//ACT AND ASSERT
		mockMvc.perform(put("/firestation").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(firestation2))).andExpect(status().isNotFound());
	
	}
	
	@Test
	void testPutFirestationWithNotValidRequest() throws Exception {
		//ARRANGE
		Firestation firestationNonValide = new Firestation();
		firestationNonValide.setStation(0);
		//ACT AND ASSERT
		mockMvc.perform(put("/firestation").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(firestationNonValide))).andExpect(status().isBadRequest());
	
	}

	@Test
	void testDeleteFirestation() throws Exception {
		//ARRANGE
		when(firestationService.getFirestation("14 place de la halle")).thenReturn(firestation2);
		//ACT AND ASSERT
		mockMvc.perform(delete("/firestation").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(firestation2))).andExpect(status().isOk());
	
		verify(firestationService).deleteFirestation("14 place de la halle");
	}
	
	@Test
	void testDeleteFirestationIfNotExist() throws Exception {
		//ARRANGE
		when(firestationService.getFirestation("14 place de la halle")).thenReturn(null);
		//ACT AND ASSERT
		mockMvc.perform(delete("/firestation").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(firestation2))).andExpect(status().isNotFound());
	
		verify(firestationService, times(0)).deleteFirestation("14 place de la halle");
	}
	
	@Test
	void testDeleteFirestationWithNotValidRequest() throws Exception {
		//ARRANGE
		Firestation firestationNonValide = new Firestation();
		firestationNonValide.setAddress("");
		//ACT AND ASSERT
		mockMvc.perform(delete("/firestation").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(firestationNonValide))).andExpect(status().isBadRequest());
	
		verify(firestationService, times(0)).deleteFirestation("14 place de la halle");
	}

	public static String asJsonString(final Object firestation2) {
		try {
			return new ObjectMapper().writeValueAsString(firestation2);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
