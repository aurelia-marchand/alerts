package com.safetynet.alerts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynet.alerts.dao.PersonDaoI;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonsInfos;
import com.safetynet.alerts.service.PersonServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(PersonServiceImpl.class)
class PersonServiceTest {
	
	@MockBean
	PersonDaoI personDao;
	
	@MockBean
	PersonsInfos personsInfos;
	
	@Autowired
	PersonServiceImpl personServiceImpl;
	
	List<Person> person = new ArrayList<>();
	Person personTest1 = new Person();
	Person personTest2 = new Person();
	Person personTest3 = new Person();
	Person personTest4 = new Person();

	@BeforeEach
	private void setUpPerTest() {
		
		personTest1 = new Person("Felicia", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6544", "jaboyd@email.com");
		personTest2 = new Person("Jonanathan", "Marrack", "29 15th St", "Culver", 97451, "841-874-6513", "drk@email.comm");
		personTest3 = new Person("Tessa", "Carman", "834 Binoc Ave", "Culver", 97451, "841-874-6512", "tenz@email.com");
		person.add(personTest1);		
		person.add(personTest2);
		person.add(personTest3);

		personsInfos.setPersons(person);
	}
	
	@Test
	void testGetListPersons() {
		when(personDao.findAllPersons()).thenReturn(person);
		
		person = personServiceImpl.GetListPersons();
		
		assertThat(person.size()).isEqualTo(3);
	}
	
	@Test
	void testGetPerson() {
		
		Person person = new Person();
		person.setFirstName("Tessa");
		person.setLastName("Carman");
		when(personDao.findPersonByFirstNameAndLastName("Tessa", "Carman")).thenReturn(person);
		
		person = personServiceImpl.getPerson("Tessa", "Carman");
		
		assertThat("Tessa").isEqualToIgnoringCase(person.getFirstName());
	}
	
	@Test
	void testDeletePerson() {

		when(personDao.findPersonByFirstNameAndLastName("Felicia", "boyd")).thenReturn(personTest1);
	    when(personDao.deletePersonByFirstNameAndLastName("Felicia", "boyd")).thenReturn(personTest1);

	    Person person = personServiceImpl.deletePerson("Felicia", "Boyd");
	   
	    assertThat(person).isNull();
	    
		verify(personDao).deletePersonByFirstNameAndLastName("Felicia", "Boyd");
		
	}
	
	@Test
	void testPostPerson() {
		when(personDao.findPersonByFirstNameAndLastName("Felicia", "boyd")).thenReturn(null);
		when(personDao.savePerson(personTest1)).thenReturn(personTest1);
		
		Person person = personServiceImpl.postPerson(personTest1);
		
		assertThat(person.getFirstName()).isEqualTo("Felicia");
		verify(personDao).savePerson(personTest1);
	}
	
	@Test
	void testPostPersonIfAlreadyExist() {
		when(personDao.findPersonByFirstNameAndLastName("Felicia", "Boyd")).thenReturn(personTest1);
		log.debug("personnetest1 : " + personTest1);
		personServiceImpl.postPerson(personTest1);
				
		verify(personDao, times(0)).savePerson(personTest1);
	}
	
	@Test
	void testPutPersonIfExist() {
		when(personDao.findPersonByFirstNameAndLastName("Felicia", "Boyd")).thenReturn(personTest1);
		
		personTest4 = new Person("Felicia", "Boyd", "14 place de la halle", "Culver", 97451, "841-874-6544", "jaboyd@email.com");
		when(personDao.savePerson(personTest4)).thenReturn(personTest4);
		
		personServiceImpl.putPerson(personTest4);
				
		verify(personDao).updatePerson(personTest4);
	}
	
	@Test
	void testPutPersonIfNotExist() {
		when(personDao.findPersonByFirstNameAndLastName("Felicia", "Boyd")).thenReturn(null);
				
		personServiceImpl.putPerson(personTest4);
		
				
		verify(personDao, times(0)).updatePerson(personTest4);
	}
	

}
