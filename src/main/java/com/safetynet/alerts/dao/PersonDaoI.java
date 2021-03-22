package com.safetynet.alerts.dao;

import java.util.List;

import com.safetynet.alerts.model.Person;


public interface PersonDaoI {

	public List<Person> findAllPersons();
	public Person getPerson(String firstName, String lastName);
	public Person postPerson(Person person);
	public Person putPerson(Person personToUpdate);
	public Person deletePerson(String firstName, String lastName);
	
}
