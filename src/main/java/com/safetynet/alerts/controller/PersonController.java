package com.safetynet.alerts.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.safetynet.alerts.exceptions.DonneeDejaExistanteException;
import com.safetynet.alerts.exceptions.DonneeIntrouvableException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonServiceI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
public class PersonController {

	@Autowired
	PersonServiceI personService;

	/**
	 * Post - add person
	 * 
	 * @return url new person
	 */
	@PostMapping("/person")
	public ResponseEntity<Void> postPerson(@Valid @RequestBody Person personToPost) {
		log.info("POST /person called");
		String firstName = personToPost.getFirstName();
		String lastName = personToPost.getLastName();

		Person person = personService.getPerson(firstName, lastName);
		if (person != null) {
			log.error("La personne existe déjà");
			throw new DonneeDejaExistanteException("La personne que vous voulez créer existe déjà !");
		} else {
			Person personNew = personService.postPerson(personToPost);
			log.info("Succès de la requête, la personne a bien été crée : "+ personNew);
			// création de l'url pour la redirection vers la personne créee
			Map<String, String> params = new HashMap<String, String>();
			params.put("firstName", firstName);
			params.put("lastName", lastName);

			URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/person")
					.queryParam("firstName", firstName).queryParam("lastName", lastName).buildAndExpand(params).toUri();

			log.info("uri created = " + location);

			return ResponseEntity.created(location).build();
		}
	}

	/**
	 * Put - Update a Person
	 * 
	 * @return url person update
	 * 
	 */
	@PutMapping("/person")
	public ResponseEntity<Void> putPerson(@Valid @RequestBody Person personToPut) {
		log.info("PUT /person/ called");

		String firstName = personToPut.getFirstName();
		String lastName = personToPut.getLastName();

		Person person = personService.getPerson(firstName, lastName);

		if (person == null) {
			throw new DonneeIntrouvableException("La personne demandée n'existe pas");

		} else {
			personToPut.setFirstName(firstName);
			personToPut.setLastName(lastName);
			personService.putPerson(personToPut);
			log.info("Requête ok, " + firstName + " " + lastName + " a bien été modifiée");

			// création de l'url pour la redirection vers la personne modifiée
			Map<String, String> params = new HashMap<String, String>();
			params.put("firstName", firstName);
			params.put("lastName", lastName);

			URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/person")
					.queryParam("firstName", firstName).queryParam("lastName", lastName).buildAndExpand(params).toUri();
			log.info("uri created = " + location);
			return ResponseEntity.created(location).build();
		}

	}

	/**
	 * Delete - Delete a Person
	 * 
	 * @return
	 * 
	 */
	@DeleteMapping("/person")
	public ResponseEntity<Void> deletePerson(@Valid @RequestBody Person personToDelete) {
		log.info("DELETE /person/ called");
		String firstName = personToDelete.getFirstName();
		String lastName = personToDelete.getLastName();
	
			Person person = personService.getPerson(firstName, lastName);
			if (person == null) {
				log.error("La requête ne peut aboutir, la personne n'existe pas !");
				throw new DonneeIntrouvableException("La personne demandée n'existe pas");
			} else {
				personService.deletePerson(firstName, lastName);
				log.info("Requête ok, " + firstName + " " + lastName + " a bien été supprimé");
				return ResponseEntity.ok(null);
			}
	}

}
