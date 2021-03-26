package com.safetynet.alerts;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alerts.controller.PersonsInfosController;
import com.safetynet.alerts.service.PersonsInfosServiceI;

@WebMvcTest(controllers = PersonsInfosController.class)
class PersonsInfosControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PersonsInfosServiceI personsInfosService;
	
	@Disabled
	@Test
	void testGetPersonsByStation() throws Exception {
		mockMvc.perform(get("/firestation/1")).andExpect(status().isOk());
	}
	

}
