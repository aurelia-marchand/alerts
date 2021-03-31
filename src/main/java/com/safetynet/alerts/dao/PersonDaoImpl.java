package com.safetynet.alerts.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.safetynet.alerts.model.Person;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
public class PersonDaoImpl implements PersonDaoI {

	@Autowired
	AccessJsonI accessJson;

	@Override
	public List<Person> findAllPersons() {
		List<Person> persons;
		try {
			persons = accessJson.getData().getPersons();
			return persons;
		} catch (Exception e) {
			log.error("erreur pendant la récupération de la liste de personne : " + e);	
		}
		return null;
	}

	@Override
	public Person findPersonByFirstNameAndLastName(String firstName, String lastName) {
		Person personToGet = null;
		List<Person> persons = accessJson.getData().getPersons();
		log.debug("persons : " + persons);
		for (Person person : persons) {
			log.debug("firstname = " + firstName + " comparé à " + person.getFirstName());
			log.debug("lastname = " + lastName + " comparé à " + person.getLastName());

			if (person.getFirstName().equalsIgnoreCase(firstName) && person.getLastName().equalsIgnoreCase(lastName)) {
				log.debug("dao renvoi : "+ person);
				personToGet = person;
				
			} 
		}
	
		return personToGet;
	}

	@Override
	public Person savePerson(Person personToPost) {
		
		Datas personsInfos = accessJson.getData();
		List<Person> persons = findAllPersons();
		Person newPerson = personToPost;
		
		persons.add(newPerson);
		personsInfos.setPersons(persons);

		accessJson.writeData(personsInfos);

		return newPerson;
	}

	@Override
	public Person updatePerson(Person personToPut) {
		Datas personsInfos = accessJson.getData();
		List<Person> persons = findAllPersons();
		Person personToUpdate = personToPut;
		int index = 0;
		for (Person person : persons) {
			if (person.getFirstName().equalsIgnoreCase(personToUpdate.getFirstName()) && person.getLastName().equalsIgnoreCase(personToUpdate.getLastName())) {
				personToUpdate = person;
				index = persons.indexOf(person);
				log.debug("index récupéré pour update");
			}
		}
	persons.set(index, personToPut);
	personsInfos.setPersons(persons);
	accessJson.writeData(personsInfos);
		return null;
	}

	@Override
	public Person deletePersonByFirstNameAndLastName(String firstName, String lastName) {
		Datas personsInfos = accessJson.getData();
		List<Person> persons = findAllPersons();
		Person personToDelete = null;
		
		for (Person person : persons) {
			if (person.getFirstName().equalsIgnoreCase(firstName) && person.getLastName().equalsIgnoreCase(lastName)) {
				personToDelete = person;
				log.debug("personne a supprimer trouvée :  "+ personToDelete.toString());
			}
		}
		persons.remove(personToDelete);

		personsInfos.setPersons(persons);

		accessJson.writeData(personsInfos);
		return null;
	}

}
