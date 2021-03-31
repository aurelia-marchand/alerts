package com.safetynet.alerts.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.exceptions.DonneeIntrouvableException;
import com.safetynet.alerts.model.PhoneAlert;
import com.safetynet.alerts.model.ChildAlert;
import com.safetynet.alerts.model.CommunityEmail;
import com.safetynet.alerts.model.DistrictPeople;
import com.safetynet.alerts.model.StationsPeople;
import com.safetynet.alerts.model.StreetPeople;
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
	@GetMapping("/firestation")
	public DistrictPeople getPersonsByStation(@RequestParam int stationNumber) {
		log.info("Requête : demande liste des personnes couvertes par la station numéro " + stationNumber);
		DistrictPeople districtDto = null;
		
			districtDto = personsInfosService.getListPersonsByStationNumber(stationNumber);

			if (districtDto.getNumberOfAdults() == 0 && districtDto.getNumberOfChildren() == 0) {
				log.error("Ce numéro de station ne retourne personne");
				throw new DonneeIntrouvableException("Ce numéro de station ne retourne personne");
			} else {
				log.info("Requête ok, liste des personnes couvertes par la station numéro : " + stationNumber
						+ " bien récupérée ");
			}

		return districtDto;
	}

	/**
	 * Get list children by address and family members
	 * 
	 * @param - address
	 * @return - children by address with firstname, lastname, old and list of
	 *         family members
	 */
	@GetMapping("/childAlert")
	public ChildAlert GetChildrenByAddress(@RequestParam String address) {
		log.info("Requête : demande liste des enfants à l'adresse " + address);
		ChildAlert childAlertDto = null;
		
			childAlertDto = personsInfosService.getListChildrenByAddress(address);

			if (childAlertDto == null) {
				log.error("Pas d'enfants à cette adresse");
				throw new DonneeIntrouvableException("Pas d'enfants à cette adresse"); 
			} else {
				log.info("Requête ok, liste des enfants bien récupérée ");
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
	@GetMapping("/phoneAlert")
	public PhoneAlert GetPhoneByStationNumber(@RequestParam int firestation) {
		log.info("Requête : demande liste des téléphones pour la station numéro " + firestation);
		PhoneAlert phoneAlertDto = null;
		
			phoneAlertDto = personsInfosService.getListPhoneByStation(firestation);

			if (phoneAlertDto == null) {
				log.error("Personne à cette station");
				throw new DonneeIntrouvableException("Auncun numéro à cette station"); 
			} else {
				log.info("Requête ok, liste des téléphones bien récupérée ");
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
	@GetMapping("/fire")
	public StreetPeople GetPeopleByAddress(@RequestParam String address) {
		log.info("Requête : demande liste des personnes à l'adresse " + address);
		StreetPeople streetDto = null;
	
			streetDto = personsInfosService.getPeopleByAddress(address);

			if (streetDto == null) {
				log.error("Personne à cette adresse");
				throw new DonneeIntrouvableException("Personne à cette adresse"); 
			} else {
				log.info("Requête ok, liste des personnes bien récupérée ");
			}

		return streetDto;
	}

	/**
	 * Get list People by stations number list with medical record and group by
	 * address
	 * 
	 * @param - list station number
	 * @return - list people with medical record group by address
	 * 
	 */
	@GetMapping("/flood/stations")
	public List<StationsPeople> GetPeopleByStation(@RequestParam List<Integer> stations) {
		log.info("Requête : demande liste des personnes pour les stations " + stations);
		List<StationsPeople> floodStationsDto = new ArrayList<>();
		
			floodStationsDto = personsInfosService.getPeopleByListStation(stations);

			if (floodStationsDto == null) {
				log.info("Personne à ces stations");
				throw new DonneeIntrouvableException("Personne à ces stations");
			} else {
				log.info("Requête ok, liste des personnes bien récupérée ");
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
	@GetMapping("/personInfo")
	public PersonInfoDto GetPersonInfo(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName) {
		log.info("Get /personInfo/firstname/lasname called");
		PersonInfoDto personInfoDto = null;
		
			personInfoDto = personsInfosService.getPersonInfo(firstName, lastName);

			if (personInfoDto == null) {
				log.error("Personne à ce nom");
				throw new DonneeIntrouvableException("Personne à ce nom");
			} else {
				log.info("Requête ok, personne bien récupérée ");
			}

		return personInfoDto;
	}

	/**
	 * Get community email
	 * 
	 * @param - city
	 * @return - emails for all people in the city
	 * 
	 */
	@GetMapping("/communityEmail")
	public CommunityEmail GetCommunityEmail(@RequestParam("city") String city) {
		log.info("Get /communityEmail/city called");
		CommunityEmail emailListDto = null;
	
			emailListDto = personsInfosService.getCommunityEmail(city);

			if (emailListDto == null) {
				log.error("personne pour cette ville");
				throw new DonneeIntrouvableException("Personne à cette ville");
			} else {
				log.info("Requête ok, liste des mails bien récupéré ");
			}

		return emailListDto;
	}

}
