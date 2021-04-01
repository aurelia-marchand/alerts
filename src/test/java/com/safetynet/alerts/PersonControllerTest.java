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
		//ARRANGE
		when(personService.getPerson("Tessa", "Carman")).thenReturn(personTest3);
		//ACT AND ASSERT
		mockMvc.perform(delete("/person").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(personTest3))).andExpect(status().isOk());

		verify(personService).deletePerson("Tessa", "Carman");
	}
	
	@Test
	void testDeletPersonIfNotExist() throws Exception {
		//ARRANGE
		when(personService.getPerson("Tessa", "Carman")).thenReturn(null);
		//ACT AND ASSERT
		mockMvc.perform(delete("/person").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(personTest2))).andExpect(status().isNotFound());

		verify(personService, times(0)).deletePerson("Tessa", "Carman");
	}
	
	@Test
	void testDeletPersonWithNotValidRequest() throws Exception {
		//ARRANGE
		Person personneInvalide = new Person();
		personneInvalide.setFirstName("");
		//ACT AND ASSERT
		mockMvc.perform(delete("/person").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(personneInvalide))).andExpect(status().isBadRequest());

	}

	@Test
	void testPutPerson() throws Exception {
		//ARRANGE
		when(personService.getPerson("Jonanathan", "Marrack")).thenReturn(personTest2);
		when(personService.putPerson(personTest2)).thenReturn(personTest2);
		//ACT AND ASSERT
		mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(personTest2))).andExpect(status().isCreated());

	}
	
	@Test
	void testPutPersonIfNotExist() throws Exception {
		//ARRANGE
		when(personService.getPerson("Jonanathan", "Marrack")).thenReturn(null);
		//ACT AND ASSERT
		mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(personTest2))).andExpect(status().isNotFound());

	}
	
	@Test
	void testPutPersonWithNotValidRequest() throws Exception {
		//ARRANGE
		Person personTest = new Person();
		personTest.setFirstName("");
		
		//ACT AND ASSERT
		mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(personTest))).andExpect(status().isBadRequest());

	}
	
	@Test
	void testPostPerson() throws Exception {
		//ARRANGE
		when(personService.getPerson("aurelia", "marchand")).thenReturn(null);
		when(personService.postPerson(personTest4)).thenReturn(personTest4);
		//ACT AND ASSERT
		mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(personTest4))).andExpect(status().isCreated());

	}
	
	@Test
	void testPostPersonIfAlreadyExist() throws Exception {
		//ARRANGE
		when(personService.getPerson("Aurélia", "Marchand")).thenReturn(personTest4);
		//ACT AND ASSERT
		mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(personTest4))).andExpect(status().isConflict());

	}
	
	@Test
	void testPostPersonWithInvalidRequest() throws Exception {
		//ARRANGE
		Person personInvalide = new Person();
		personInvalide.setFirstName("");
		//ACT AND ASSERT
		mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(personInvalide))).andExpect(status().isBadRequest());

	}

	public static String asJsonString(final Object objet) {
		try {
			return new ObjectMapper().writeValueAsString(objet);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
