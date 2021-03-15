package com.safetynet.alerts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PersonController {

	@Autowired
	PersonServiceImpl personService;

	/**
	 * Read all persons-
	 * 
	 * @return list of persons
	 */
	@GetMapping("/persons")
	public List<Person> GetPersons() {
		return personService.GetListPersons();
	}

	/**
	 * Read - Get one person
	 * 
	 * @return A person
	 */
	@GetMapping("/person/{firstName}/{lastName}")
	public Person getPerson(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		// existant pour test : Roger Boyd / Tessa Carman / Shawna Stelzer
		return personService.getPerson(firstName, lastName);

	}

}
