package com.safetynet.alerts.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonsInfos;

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
	public Person getPerson(String firstName, String lastName) {
		List<Person> persons = accessJson.getData().getPersons();
		for (Person person : persons) {
			if (person.getFirstName().equalsIgnoreCase(firstName) && person.getLastName().equalsIgnoreCase(lastName)) {
				log.debug("dao renvoi : "+ person);
				return person;
			}
		}
		return null;
	}

	@Override
	public Person postPerson(Person personToPost) {
		
		PersonsInfos personsInfos = accessJson.getData();
		List<Person> persons = findAllPersons();
		Person newPerson = personToPost;
		
		persons.add(newPerson);
		personsInfos.setPersons(persons);

		accessJson.writeData(personsInfos);

		return null;
	}

	@Override
	public Person putPerson(Person personToPut) {
		PersonsInfos personsInfos = accessJson.getData();
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
	public Person deletePerson(String firstName, String lastName) {
		PersonsInfos personsInfos = accessJson.getData();
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
