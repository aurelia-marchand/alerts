package com.safetynet.alerts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynet.alerts.dao.PersonDaoImpl;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonsInfos;
import com.safetynet.alerts.service.PersonServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@RunWith(SpringRunner.class)
@WebMvcTest(PersonServiceImpl.class)
class PersonServiceTest {
	
	@MockBean
	PersonDaoImpl personDaoImpl;
	
	@MockBean
	PersonsInfos personsInfos;
	
	@Autowired
	PersonServiceImpl personServiceImpl;
	
	List<Person> person = new ArrayList<>();

	@BeforeEach
	private void setUpPerTest() {
		
		Person personTest1 = new Person("Felicia", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6544", "jaboyd@email.com");
		Person personTest2 = new Person("Jonanathan", "Marrack", "29 15th St", "Culver", 97451, "841-874-6513", "drk@email.comm");
		Person personTest3 = new Person("Tessa", "Carman", "834 Binoc Ave", "Culver", 97451, "841-874-6512", "tenz@email.com");
		person.add(personTest1);		
		person.add(personTest2);
		person.add(personTest3);

		personsInfos.setPersons(person);
	}
	
	@Test
	void testGetListPersons() {
		when(personDaoImpl.findAllPersons()).thenReturn(person);
		
		personServiceImpl.GetListPersons();
		
		assertThat(person.size()).isEqualTo(3);
	}
	
	@Test
	void testPerson() {
		
		Person person = new Person();
		person.setFirstName("Tessa");
		person.setLastName("Carman");
		//personsInfos.setPersons(person);
		when(personDaoImpl.getPerson("Tessa", "Carman")).thenReturn(person);
		
		person = personServiceImpl.getPerson("Tessa", "Carman");
		
		assertThat("Tessa").isEqualToIgnoringCase(person.getFirstName());
	}
	
	@Test
	void testDeletePerson() {

		when(personDaoImpl.findAllPersons()).thenReturn(person);
	    
		log.debug("Taille du tableau : " + person.size());

		try {
			personServiceImpl.deletePerson("Felicia", "Boyd");
			log.debug("delete");
			log.debug("Taille du tableau : " + person.size());
			
		} catch (Exception e) {
			log.debug("pas delete");
			e.printStackTrace();
		}
		
		//assertThat(person.size()).isEqualTo(2);
		log.debug("Taille du tableau : " + person.size());
		
		verify(personDaoImpl).deletePerson("Felicia", "Boyd");
		
	}
	

}
