package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.model.Person;

public interface PersonServiceI {

	List<Person> GetListPersons();
	Person getPerson(String firstName, String lastName);
	Person postPerson(String firstName, String lastName, String address, String city, int zip, String phone, String email);
	Person putPerson(); //TODO Ajout param
	Person deletePerson(String firstName, String lastName);
}
