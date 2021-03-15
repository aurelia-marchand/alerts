package com.safetynet.alerts.controller;

import org.apache.maven.doxia.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.PeopleCoveredDto;
import com.safetynet.alerts.service.PersonsInfosServiceI;

@RestController
public class PersonsInfosController {

	@Autowired
	PersonsInfosServiceI personsInfosService;

	/**
		 * Get list people covered by station and the number of adults and children 
		 * @param - station number
		 * @return - people covered by station, firstname, lastname, address, phone and the number of adults and children
		 */
		@GetMapping("/firestation/{station}")
		public PeopleCoveredDto GetPersonsByStation(@PathVariable int station) {
			
			return personsInfosService.GetListPersonsByStationNumber(station);
			}

}
