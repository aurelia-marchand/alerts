package com.safetynet.alerts;

import static org.mockito.Mockito.times;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.MedicalRecordController;
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
		//ARRANGE
		when(medicalRecordService.getMedicalRecord("Felicia", "Boyd")).thenReturn(med1);
		//ACT AND ASSERT
		mockMvc.perform(delete("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(med1))).andExpect(status().isOk());
		verify(medicalRecordService).deleteMedicalRecord("Felicia", "Boyd");
	}
	
	@Test
	void testDeletMedicalRecordIfNotExist() throws Exception {
		//ARRANGE
		when(medicalRecordService.getMedicalRecord("Felicia", "Boyd")).thenReturn(null);
		//ACT AND ASSERT
		mockMvc.perform(delete("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(med1))).andExpect(status().isNotFound());
		verify(medicalRecordService, times(0)).deleteMedicalRecord("Felicia", "Boyd");
	}
	
	@Test
	void testDeletMedicalRecordWithNotValidRequest() throws Exception {
		//ARRANGE
		MedicalRecord medNonValid = new MedicalRecord();
		medNonValid.setFirstName("");
		//ACT AND ASSERT
		mockMvc.perform(delete("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(medNonValid))).andExpect(status().isBadRequest());
	}
	
	@Test
	void testPutMedicalRecord() throws Exception {
		//ARRANGE
		when(medicalRecordService.getMedicalRecord("Jonhathan", "Marrack")).thenReturn(med2);
		when(medicalRecordService.putMedicalRecord(med2)).thenReturn(med2);
		//ACT AND ASSERT
		mockMvc.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(med2))).andExpect(status().isCreated());
	}
	
	@Test
	void testPutMedicalRecordIfNotExist() throws Exception {
		//ARRANGE
		when(medicalRecordService.getMedicalRecord("Jonhathan", "Marrack")).thenReturn(null);
		//ACT AND ASSERT
		mockMvc.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(med2))).andExpect(status().isNotFound());
	}
	
	@Test
	void testPutMedicalRecordWithNotValidRequest() throws Exception {
		//ARRANGE
		MedicalRecord medNonValid = new MedicalRecord();
		medNonValid.setFirstName("");
		//ACT AND ASSERT
		mockMvc.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(medNonValid))).andExpect(status().isBadRequest());
	}
	
	@Test
	void testPostMedicalRecord() throws Exception {
		//ARRANGE
		when(medicalRecordService.getMedicalRecord("Tony", "Cooper")).thenReturn(null);
		when(medicalRecordService.postMedicalRecord(med3)).thenReturn(med3);
		//ACT AND ASSERT
		mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(med3))).andExpect(status().isCreated());

	}
	
	@Test
	void testPostMedicalRecordIfAlreadyExist() throws Exception {
		//ARRANGE
		when(medicalRecordService.getMedicalRecord("Tony", "Cooper")).thenReturn(med3);
		//ACT AND ASSERT
		mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(med3))).andExpect(status().isConflict());

	}
	
	@Test
	void testPostMedicalRecordWithInvalidRequest() throws Exception {
		//ARRANGE
		MedicalRecord medNonValid = new MedicalRecord();
		medNonValid.setFirstName("");
		//ACT AND ASSERT
		mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(medNonValid))).andExpect(status().isBadRequest());

	}
	
	public static String asJsonString(final Object objet) {
		try {
			return new ObjectMapper().writeValueAsString(objet);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
