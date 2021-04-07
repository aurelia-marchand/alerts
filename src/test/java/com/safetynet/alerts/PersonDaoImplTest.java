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
import com.safetynet.alerts.dao.AccessJsonI;
import com.safetynet.alerts.dao.Datas;
import com.safetynet.alerts.dao.PersonDaoImpl;
import com.safetynet.alerts.model.Person;

import nl.jqno.equalsverifier.EqualsVerifier;

@WebMvcTest(PersonDaoImpl.class)
class PersonDaoImplTest {

	@Autowired
	PersonDaoImpl personDaoImpl;

	@MockBean
	AccessJsonI accessJsonI;

	List<Person> persons = new ArrayList<>();
	Datas personsInfos = new Datas();

	@BeforeEach
	private void setUpPerTest() {
		Person personTest1 = new Person("Felicia", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6544",
				"jaboyd@email.com");
		Person personTest2 = new Person("Jonanathan", "Marrack", "29 15th St", "Culver", 97451, "841-874-6513",
				"drk@email.comm");
		Person personTest3 = new Person("Tessa", "Carman", "834 Binoc Ave", "Culver", 97451, "841-874-6512",
				"tenz@email.com");
		persons.add(personTest1);
		persons.add(personTest2);
		persons.add(personTest3);

		personsInfos.setPersons(persons);
	}

	@Test
	void testFindAllPersons() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		// ACT
		List<Person> result = personDaoImpl.findAllPersons();
		// ASSERT
		assertThat(result.size()).isEqualTo(3);
	}

	@Test
	void testGetPerson() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		// ACT
		Person result = personDaoImpl.findPersonByFirstNameAndLastName("Felicia", "Boyd");
		// ASSERT
		assertThat("Felicia").isEqualToIgnoringCase(result.getFirstName());
	}

	@Test
	void testSavePerson() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		Person personToPost = new Person("Aurelia", "Marchand", "place de la halle", "magny", 97420, "999-999-999",
				"aure@email.com");

		// ACT
		Person newPerson = personDaoImpl.savePerson(personToPost);

		// ASSERT
		assertThat(personsInfos.getPersons()).contains(newPerson);
	}

	@Test
	void testUpdatePerson() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		Person personToPut = new Person("Felicia", "Boyd", "Avenue du val", "magny", 97420, "999-999-999",
				"aure@email.com");

		// ACT
		personDaoImpl.updatePerson(personToPut);

		// ASSERT
		List<Person> persons = personsInfos.getPersons();
		assertThat(persons).contains(personToPut);
		assertThat(personToPut.getAddress()).contains("Avenue du val");

		for (Person person : persons) {
			if (person.getFirstName() == "Felicia") {
				assertThat(person.getAddress()).isEqualTo(personToPut.getAddress());
			}
		}
	}

	@Test
	void testDeletePerson() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		// ACT
		personDaoImpl.deletePersonByFirstNameAndLastName("Felicia", "Boyd");
		List<Person> result = personsInfos.getPersons();
		// ASSERT
		assertThat(2).isEqualTo(result.size());
	}

	@Test
	public void simpleEqualsContract() {
		EqualsVerifier.simple().forClass(PersonDaoImpl.class).verify();
	}

	@Test
	public void testToString() {
		ToStringVerifier.forClass(PersonDaoImpl.class).withClassName(NameStyle.SIMPLE_NAME).verify();
	}

}
