package com.safetynet.alerts.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.dto.FireListDto;
import com.safetynet.alerts.dto.FloodStationsDto;
import com.safetynet.alerts.dto.PeopleByAddressDto;
import com.safetynet.alerts.dto.PeopleCoveredDto;
import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.dto.PhoneAlertDto;
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
	
	/**
	 * Get list children by address and family members
	 * 
	 * @param - address
	 * @return - children by address with firstname, lastname, old
	 *         and list of family members
	 */
	@GetMapping("/childAlert/{address}")
	public ChildAlertDto GetChildrenByAddress(@PathVariable String address) {
		log.info("Requête : demande liste des enfants à l'adresse " + address);
		ChildAlertDto childAlertDto = null;
		try {
			childAlertDto = personsInfosService.getListChildrenByAddress(address);

			if (childAlertDto == null) {
				log.info("Pas d'enfants à cette adresse");
			} else {
				log.info("Requête ok, liste des enfants bien récupérée ");
			}

		} catch (Exception e) {
			log.error("erreur lors de la récupération de la liste des enfants, erreur : " + e);
			e.printStackTrace();
		}

		return childAlertDto;
	}
	
	/**
	 * Get phone people covered by station number
	 * 
	 * @param - station number
	 * @return - list phones
	 *         
	 */
	@GetMapping("/phoneAlert/{station}")
	public PhoneAlertDto GetPhoneByStationNumber(@PathVariable int station) {
		log.info("Requête : demande liste des téléphones pour la station numéro " + station);
		PhoneAlertDto phoneAlertDto = null;
		try {
			phoneAlertDto = personsInfosService.getListPhoneByStation(station);

			if (phoneAlertDto == null) {
				log.info("Personne à cette station");
			} else {
				log.info("Requête ok, liste des téléphones bien récupérée ");
			}

		} catch (Exception e) {
			log.error("erreur lors de la récupération de la liste des téléphone, erreur : " + e);
			e.printStackTrace();
		}

		return phoneAlertDto;
	}
	
	/**
	 * Get list People by address with medical record and station number
	 * 
	 * @param - address
	 * @return - list people with medical record
	 *         
	 */
	@GetMapping("/fire/{address}")
	public FireListDto GetPeopleByAddress(@PathVariable String address) {
		log.info("Requête : demande liste des personnes à l'adresse " + address);
		FireListDto fireListDto = null;
		try {
			fireListDto = personsInfosService.getPeopleByAddress(address);

			if (fireListDto == null) {
				log.info("Personne à cette adresse");
			} else {
				log.info("Requête ok, liste des personnes bien récupérée ");
			}

		} catch (Exception e) {
			log.error("erreur lors de la récupération de la liste des personnes, erreur : " + e);
			e.printStackTrace();
		}

		return fireListDto;
	}
	
	/**
	 * Get list People by stations number list with medical record and group by address
	 * 
	 * @param - list station number
	 * @return - list people with medical record group by address
	 *         
	 */
	@GetMapping("/flood/stations")
	public List<FloodStationsDto> GetPeopleByStation(@RequestParam List<Integer> stations) {
		log.info("Requête : demande liste des personnes pour les stations " + stations);
		 List<FloodStationsDto> floodStationsDto  = new ArrayList<>();
		try {
			floodStationsDto = personsInfosService.getPeopleByListStation(stations);

			if (floodStationsDto == null) {
				log.info("Personne à ces stations");
			} else {
				log.info("Requête ok, liste des personnes bien récupérée ");
			}

		} catch (Exception e) {
			log.error("erreur lors de la récupération de la liste des personnes, erreur : " + e);
			e.printStackTrace();
		}

		return floodStationsDto;
	}
	
	/**
	 * Get person info by this firstname and lastname
	 * 
	 * @param - firstname and lastname
	 * @return - person info, name, address, old, email and medical record
	 *         
	 */
	@GetMapping("/personInfo/{firstName}/{lastName}")
	public PersonInfoDto GetPersonInfo(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		log.info("Get /personInfo/firstname/lasname called");
		 PersonInfoDto personInfoDto = null;
		try {
			personInfoDto = personsInfosService.getPersonInfo(firstName, lastName);

			if (personInfoDto == null) {
				log.info("Personne à ce nom");
			} else {
				log.info("Requête ok, personne bien récupérée ");
			}

		} catch (Exception e) {
			log.error("erreur lors de la récupération de la personne, erreur : " + e);
			e.printStackTrace();
		}

		return personInfoDto;
	}
	
	

}
