package com.safetynet.alerts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.safetynet.alerts.dao.AccessJsonImpl;
import com.safetynet.alerts.dao.PersonDaoImpl;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonsInfos;

import nl.jqno.equalsverifier.EqualsVerifier;

@WebMvcTest(PersonDaoImpl.class)
class PersonDaoImplTest {

	@Autowired
	PersonDaoImpl personDaoImpl;
	
	@MockBean
	AccessJsonImpl accessJsonImpl;
	
	List<Person> persons = new ArrayList<>();
	PersonsInfos personsInfos = new PersonsInfos();
	
	@BeforeEach
private void setUpPerTest() {
		Person personTest1 = new Person("Felicia", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6544", "jaboyd@email.com");
		Person personTest2 = new Person("Jonanathan", "Marrack", "29 15th St", "Culver", 97451, "841-874-6513", "drk@email.comm");
		Person personTest3 = new Person("Tessa", "Carman", "834 Binoc Ave", "Culver", 97451, "841-874-6512", "tenz@email.com");
		persons.add(personTest1);		
		persons.add(personTest2);
		persons.add(personTest3);

		personsInfos.setPersons(persons);
	}

	@Test
	void testFindAllPersons() {
		when(accessJsonImpl.getData()).thenReturn(personsInfos);
		
		List<Person> result = personDaoImpl.findAllPersons();
		
		assertThat(result.size()).isEqualTo(3);
	}
	
	@Test
	void testGetPerson() {
		when(accessJsonImpl.getData()).thenReturn(personsInfos);
		
		Person result = personDaoImpl.getPerson("Felicia", "Boyd");
		
		assertThat("Felicia").isEqualToIgnoringCase(result.getFirstName());
	}
	
	@Test
	void testDeletePerson() {
		when(accessJsonImpl.getData()).thenReturn(personsInfos);
		
		List <Person> result1 =personsInfos.getPersons();
		assertThat(3).isEqualTo(result1.size());
		personDaoImpl.deletePerson("Felicia", "Boyd");
		
		List <Person> result = personsInfos.getPersons();
		
		assertThat(2).isEqualTo(result.size());
	}
	
	@Test
	public void simpleEqualsContract() {
	    EqualsVerifier.simple().forClass(PersonDaoImpl.class).verify();
	}
	
	@Test
	public void testToString()
	{
	    ToStringVerifier.forClass(PersonDaoImpl.class)
	                    .withClassName(NameStyle.SIMPLE_NAME)
	                    .verify();
	}

}
