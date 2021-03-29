package com.safetynet.alerts;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.MedicalRecordController;
import com.safetynet.alerts.dto.DistrictDto;
import com.safetynet.alerts.dto.DistrictPersonsDto;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordServiceI;

@WebMvcTest(controllers = MedicalRecordController.class)
class MedicalRecordControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MedicalRecordServiceI medicalRecordService;

	MedicalRecord med1 = new MedicalRecord();
	MedicalRecord med2 = new MedicalRecord();
	MedicalRecord med3 = new MedicalRecord();
	List<MedicalRecord> medRecords = new ArrayList<>();
	
	
	@BeforeEach
	private void setUpPerTest() {
		med1.setFirstName("Felicia");
		med1.setLastName("Boyd");
		med1.setBirthdate("01/08/1986");
		med1.setMedications(new ArrayList<>(Arrays.asList("tetracyclaz:650mg")));
		med1.setAllergies(new ArrayList<>(Arrays.asList("xilliathal")));
		
		med2.setFirstName("Jonhathan");
		med2.setLastName("Marrack");
		med2.setBirthdate("01/03/1989");
		
		med3.setFirstName("Tony");
		med3.setLastName("Cooper");
		med3.setBirthdate("03/06/1994");
		med3.setMedications(new ArrayList<>(Arrays.asList("hydrapermazol:300mg", "dodoxadin:30mg")));
		med3.setAllergies(new ArrayList<>(Arrays.asList("shellfish")));
		
		medRecords.add(med1);
		medRecords.add(med2);
		medRecords.add(med3);
		
		
	}

	@Test
	void testDeletMedicalRecord() throws Exception {
		when(medicalRecordService.getMedicalRecord("Felicia", "Boyd")).thenReturn(med1);
		mockMvc.perform(delete("/medicalRecord?firstName=Felicia&lastName=Boyd")).andExpect(status().isOk());

		verify(medicalRecordService).deleteMedicalRecord("Felicia", "Boyd");
	}
	
	@Test
	void testPutMedicalRecord() throws Exception {
		when(medicalRecordService.getMedicalRecord("Jonanathan", "Marrack")).thenReturn(med2);
		when(medicalRecordService.putMedicalRecord(med2)).thenReturn(med2);

		mockMvc.perform(put("/medicalRecord?firstName=Jonanathan&lastName=Marrack").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(med2))).andExpect(status().isCreated());

	}
	
	@Test
	void testPostMedicalRecord() throws Exception {
		when(medicalRecordService.getMedicalRecord("Tony", "Cooper")).thenReturn(null);
		when(medicalRecordService.postMedicalRecord(med3)).thenReturn(med3);

		mockMvc.perform(post("/medicalRecord?firstName=Tony&lastName=Cooper").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(med3))).andExpect(status().isCreated());

	}
	
	public static String asJsonString(final Object objet) {
		try {
			return new ObjectMapper().writeValueAsString(objet);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
