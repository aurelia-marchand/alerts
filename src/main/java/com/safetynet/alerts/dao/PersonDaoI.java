package com.safetynet.alerts.dao;

import java.util.List;

import com.safetynet.alerts.model.Person;


public interface PersonDaoI {

	public List<Person> findAllPersons();
	public Person findPersonByFirstNameAndLastName(String firstName, String lastName);
	public Person savePerson(Person person);
	public Person updatePerson(Person personToUpdate);
	public Person deletePersonByFirstNameAndLastName(String firstName, String lastName);
	
}
