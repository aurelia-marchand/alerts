package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dao.PersonDaoI;
import com.safetynet.alerts.model.Person;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class PersonServiceImpl implements PersonServiceI{

	@Autowired
	private PersonDaoI personDao;
	
	@Override
	public List<Person> GetListPersons() {
		log.debug("étape service get list persons");
		return personDao.findAllPersons();
	}
	
	@Override
	public Person getPerson(String firstName, String lastName) {
		log.debug("service demande au dao : " + firstName + " " + lastName);
		return personDao.getPerson(firstName, lastName);
	}

	@Override
	public Person postPerson(Person person) {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		
		Person personToPost = personDao.getPerson(firstName, lastName);
		
		if(personToPost == null) {
			log.debug("envoi de la personne à créer au dao");
			personDao.postPerson(person);
		} else {
			log.error("Personne déjà existante");
		}
		
		return person;
	}

	@Override
	public Person putPerson(Person personToPut) {

		Person person = personDao.getPerson(personToPut.getFirstName(), personToPut.getLastName());
		
		//Mise à jour de la personne selon les valeurs reçues
		String address = personToPut.getAddress();
		if(address != null) {
			log.debug("reçu valeur adresse :" + address);
			person.setAddress(address);
		}
		String city = personToPut.getCity();
		if(city != null) {
			log.debug("reçu valeur city :" + city);
			person.setCity(city);
		}
		int zip = personToPut.getZip();
		if(zip != 0) {
			log.debug("reçu valeur zip :" + zip);
			person.setZip(zip);
		}
		String phone = personToPut.getPhone();
		if(phone != null) {
			log.debug("reçu valeur phone :" + phone);
			person.setPhone(phone);
		}
		String email = personToPut.getEmail();
		if(email != null) {
			log.debug("reçu valeur email :" + email);
			person.setEmail(email);
		}
		
		personDao.putPerson(person);		
		return person;
	}

	@Override
	public Person deletePerson(String firstName, String lastName) {
		
		Person personToDelete = personDao.getPerson(firstName, lastName);
		
		log.debug("envoi de la personne à supprimer au dao");
		personDao.deletePerson(firstName, lastName);
		
		return personToDelete;
	}

}
