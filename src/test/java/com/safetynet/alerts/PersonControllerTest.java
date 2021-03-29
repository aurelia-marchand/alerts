package com.safetynet.alerts;

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
import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonServiceI;

@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersonServiceI personService;

	Person personTest1 = new Person();
	Person personTest2 = new Person();
	Person personTest3 = new Person();
	Person personTest4 = new Person();
	List<Person> persons = new ArrayList<>();

	@BeforeEach
	private void setUpPerTest() {

		personTest1 = new Person("Felicia", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6544",
				"jaboyd@email.com");
		personTest2 = new Person("Jonanathan", "Marrack", "29 15th St", "Culver", 97451, "841-874-6513",
				"drk@email.comm");
		personTest3 = new Person("Tessa", "Carman", "834 Binoc Ave", "Culver", 97451, "841-874-6512", "tenz@email.com");
		personTest4 = new Person("Aurélia", "Marchand", "place de la halle", "magny", 95420, "999-999-999", "aure@email.com");
		persons.add(personTest1);
		persons.add(personTest2);
		persons.add(personTest3);
	}

	@Test
	void testDeletPerson() throws Exception {
		when(personService.getPerson("Tessa", "Carman")).thenReturn(personTest3);
		mockMvc.perform(delete("/person?firstName=Tessa&lastName=Carman")).andExpect(status().isOk());

		verify(personService).deletePerson("Tessa", "Carman");
	}

	@Test
	void testPutPerson() throws Exception {
		when(personService.getPerson("Jonanathan", "Marrack")).thenReturn(personTest2);
		when(personService.putPerson(personTest2)).thenReturn(personTest2);

		mockMvc.perform(put("/person?firstName=Jonanathan&lastName=Marrack").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(personTest2))).andExpect(status().isCreated());

	}
	
	@Test
	void testPostPerson() throws Exception {
		when(personService.getPerson("aurelia", "marchand")).thenReturn(null);
		when(personService.postPerson(personTest4)).thenReturn(personTest4);

		mockMvc.perform(post("/person?firstName=aurelia&lastName=marchand").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(personTest4))).andExpect(status().isCreated());

	}

	public static String asJsonString(final Object objet) {
		try {
			return new ObjectMapper().writeValueAsString(objet);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
