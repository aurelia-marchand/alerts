package com.safetynet.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.PeopleCoveredDto;
import com.safetynet.alerts.service.PersonsInfosServiceI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PersonsInfosController {

	@Autowired
	PersonsInfosServiceI personsInfosService;

	/**
	 * Get list people covered by station and the number of adults and children
	 * 
	 * @param - station number
	 * @return - people covered by station, firstname, lastname, address, phone and
	 *         the number of adults and children
	 */
	@GetMapping("/firestation/{station}")
	public PeopleCoveredDto GetPersonsByStation(@PathVariable int station) {
		log.info("Requête : demande liste des personnes couvertes par la station numéro " + station);
		PeopleCoveredDto peopleCoverdeDto = null;
		try {
			peopleCoverdeDto = personsInfosService.getListPersonsByStationNumber(station);

			if (peopleCoverdeDto.getNumberOfAdults() == 0 && peopleCoverdeDto.getNumberOfChildren() == 0) {
				log.error("Ce numéro de station ne retourne personne");
			} else {
				log.info("Requête ok, liste des personnes couvertes par la station numéro : " + station
						+ " bien récupérée ");
			}

		} catch (Exception e) {
			log.error("erreur lors de la récupération de la liste des personnes couvertes, erreur : " + e);
			e.printStackTrace();
		}

		return peopleCoverdeDto;
	}

}
