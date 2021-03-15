package com.safetynet.alerts.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.safetynet.alerts.model.Person;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Configuration
public class PersonDaoImpl implements PersonDaoI {

	@Autowired
	AccessJsonI accessJson;
	
	@Override
	public List<Person> findAllPersons() {
		List<Person> persons = accessJson.getData().getPersons();
		return persons;
	}
	
	@Override
	public Person getPerson(String firstName, String lastName) {
		List<Person> persons = accessJson.getData().getPersons();
		for(Person person : persons) {
			if(person.getFirstName().equalsIgnoreCase(firstName) && person.getLastName().equalsIgnoreCase(lastName)) {
				
				return person;
			} 
		}
		return null;

	}

	@Override
	public Person postPerson(String firstName, String lastName, String address, String city, int zip, String phone,
			String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person putPerson() {
		// TODO Voir ajout param interface...
		return null;
	}

	@Override
	public Person deletePerson(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
