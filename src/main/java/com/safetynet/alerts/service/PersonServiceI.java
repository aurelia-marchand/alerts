package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;


public interface PersonServiceI {

	List<Person> GetListPersons();
	Person getPerson(String firstName, String lastName);
	Person postPerson(Person person);
	Person putPerson(Person person);
	Person deletePerson(String firstName, String lastName);
	
}
