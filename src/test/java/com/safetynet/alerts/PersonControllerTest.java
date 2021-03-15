package com.safetynet.alerts;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.service.PersonServiceImpl;

@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PersonServiceImpl personService;

	@Test
	void testGetPersons() throws Exception {
		mockMvc.perform(get("/persons")).andExpect(status().isOk());
	}
	
	@Test
	void testGetPerson() throws Exception {
		mockMvc.perform(get("/person/Tessa/Carman")).andExpect(status().isOk());
	}

}
