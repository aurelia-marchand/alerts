package com.safetynet.alerts;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonServiceI;


@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PersonServiceI personService;
	
	
	
	Person personTest3 = new Person();
	List<Person> persons = new ArrayList<>();
	
	@BeforeEach
	private void setUpPerTest() {
		
		Person personTest1 = new Person("Felicia", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6544", "jaboyd@email.com");
		Person personTest2 = new Person("Jonanathan", "Marrack", "29 15th St", "Culver", 97451, "841-874-6513", "drk@email.comm");
		personTest3 = new Person("Tessa", "Carman", "834 Binoc Ave", "Culver", 97451, "841-874-6512", "tenz@email.com");
		 persons.add(personTest1);
		 persons.add(personTest2);
		 persons.add(personTest3);
	}
	

	@Test
	void testGetPersons() throws Exception {
		mockMvc.perform(get("/persons"))
		.andExpect(status().isOk());
	}
	
	@Test
	void testGetPerson() throws Exception {
		
		mockMvc.perform(get("/person/Tessa/Carman")).andExpect(status().isOk());
	}
	
	
	@Test
	void testDeletPerson() throws Exception {
		when(personService.getPerson("Tessa", "Carman")).thenReturn(personTest3);
		mockMvc.perform(delete("/person/Tessa/Carman")).andExpect(status().isOk());
		
		verify(personService).deletePerson("Tessa", "Carman");
	}
	
	
	
	
	
}
