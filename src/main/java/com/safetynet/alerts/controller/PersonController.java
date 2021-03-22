package com.safetynet.alerts.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.safetynet.alerts.exceptions.PersonDejaExistanteException;
import com.safetynet.alerts.exceptions.PersonIntrouvableException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonServiceI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PersonController {

	@Autowired
	PersonServiceI personService;

	/**
	 * Read all persons-
	 * 
	 * @return list of persons
	 */
	@GetMapping("/persons")
	public List<Person> getPersons() {
		log.info("GET /persons called");
		List<Person> persons = personService.GetListPersons();
		log.info("result : "+ persons);
		return persons;
	}

	/**
	 * Read - Get one person
	 * 
	 * @return A person
	 */
	@GetMapping("/person/{firstName}/{lastName}")
	public Person getPerson(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		log.info("GET /person/{"+ firstName +"}/{"+lastName+"} called");

		Person person = personService.getPerson(firstName, lastName);
		log.debug("personne : " + person);
		if (person == null) {
			log.error("personne introuvable");
			throw new PersonIntrouvableException("La personne demandée n'existe pas");
		}
			
		return person;
	}

	/**
	 * Delete - Delete a Person
	 * 
	 */
	@DeleteMapping("/person/{firstName}/{lastName}")
	public void deletePerson(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		log.info("DELETE /person/{firstName}/{lastName} called");

		Person person = personService.getPerson(firstName, lastName);

		if (person == null) {
			log.error("personne introuvable");
			throw new PersonIntrouvableException("La personne demandée n'existe pas");
		} else {
			personService.deletePerson(firstName, lastName);
			log.info("Requête ok, " + firstName + " " + lastName + " a bien été supprimé");
		}
	}

	/**
	 * Put - Update a Person
	 * 
	 * @return url person update
	 * 
	 */
	@PutMapping("/person/{firstName}/{lastName}")
	public ResponseEntity<Void> putPerson(@PathVariable("firstName") String firstName,
			@PathVariable("lastName") String lastName, @RequestBody Person personToPut) {

		log.info("PUT /person/"+firstName+"/+"+lastName+" called");

		Person person = personService.getPerson(firstName, lastName);

		if (person == null) {
			log.error("Personne introuvable");
			throw new PersonIntrouvableException("La personne demandée n'existe pas");
		} 
		else {
			personToPut.setFirstName(firstName);
			personToPut.setLastName(lastName);
			personService.putPerson(personToPut);
			log.info("Requête ok, " + firstName + " " + lastName + " a bien été modifiée");
			
			// création de l'url pour la redirection vers la personne modifiée
			Map<String, String> params = new HashMap<String, String>();
			params.put("firstName", firstName);
			params.put("lastName", lastName);

			URI location = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/person/{firstName}/{lastName}")
					.buildAndExpand(params).toUri();
			return ResponseEntity.created(location).build();
		}
	}

	/**
	 * Post - add person
	 * 
	 * @return url new person
	 */
	@PostMapping("/person")
	public ResponseEntity<Void> postPerson(@RequestBody Person personToPost) {
		log.info("POST /person called");
		String firstName = personToPost.getFirstName();
		String lastName = personToPost.getLastName();

		Person person = personService.getPerson(firstName, lastName);
		if (person != null) {
			log.error("Personne déjà existante");
			throw new PersonDejaExistanteException("La personne que vous voulez créer existe déjà !");
		} else {

			personService.postPerson(personToPost);
			
			// création de l'url pour la redirection vers la personne créee
			Map<String, String> params = new HashMap<String, String>();
			params.put("firstName", firstName);
			params.put("lastName", lastName);

			URI location = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/person/{firstName}/{lastName}")
					.buildAndExpand(params).toUri();

			log.info("uri = " + location);

			return ResponseEntity.created(location).build();

		}
	}

}
