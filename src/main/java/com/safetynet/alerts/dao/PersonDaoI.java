package com.safetynet.alerts.dao;

import java.util.List;

import com.safetynet.alerts.model.Person;

public interface PersonDaoI {

	public List<Person> findAllPersons();
	public Person getPerson(String firstName, String lastName);
	public Person postPerson(String firstName, String lastName, String address, String city, int zip, String phone, String email);
	public Person putPerson(); //TODO ajouter paramètres pour modifications et identifiants de personne
	public Person deletePerson(String firstName, String lastName);
	
}
