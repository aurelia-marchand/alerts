package com.safetynet.alerts.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.AlertPhoneDto;
import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.dto.CommunityEmailDto;
import com.safetynet.alerts.dto.DistricDto;
import com.safetynet.alerts.dto.StationsDto;
import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.dto.StreetDto;
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
	public DistricDto GetPersonsByStation(@RequestParam int stationNumber) {
		log.info("Requête : demande liste des personnes couvertes par la station numéro " + stationNumber);
		DistricDto peopleCoverdeDto = null;
		try {
			peopleCoverdeDto = personsInfosService.getListPersonsByStationNumber(stationNumber);

			if (peopleCoverdeDto.getNumberOfAdults() == 0 && peopleCoverdeDto.getNumberOfChildren() == 0) {
				log.error("Ce numéro de station ne retourne personne");
			} else {
				log.info("Requête ok, liste des personnes couvertes par la station numéro : " + stationNumber
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
	 * @return - children by address with firstname, lastname, old and list of
	 *         family members
	 */
	@GetMapping("/childAlert")
	public ChildAlertDto GetChildrenByAddress(@RequestParam String address) {
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
	@GetMapping("/phoneAlert")
	public AlertPhoneDto GetPhoneByStationNumber(@RequestParam int firestation) {
		log.info("Requête : demande liste des téléphones pour la station numéro " + firestation);
		AlertPhoneDto phoneAlertDto = null;
		try {
			phoneAlertDto = personsInfosService.getListPhoneByStation(firestation);

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
	@GetMapping("/fire")
	public StreetDto GetPeopleByAddress(@RequestParam String address) {
		log.info("Requête : demande liste des personnes à l'adresse " + address);
		StreetDto fireListDto = null;
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
	 * Get list People by stations number list with medical record and group by
	 * address
	 * 
	 * @param - list station number
	 * @return - list people with medical record group by address
	 * 
	 */
	@GetMapping("/flood/stations")
	public List<StationsDto> GetPeopleByStation(@RequestParam List<Integer> stations) {
		log.info("Requête : demande liste des personnes pour les stations " + stations);
		List<StationsDto> floodStationsDto = new ArrayList<>();
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
	@GetMapping("/personInfo")
	public PersonInfoDto GetPersonInfo(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName) {
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

	/**
	 * Get community email
	 * 
	 * @param - city
	 * @return - emails for all people in the city
	 * 
	 */
	@GetMapping("/communityEmail")
	public CommunityEmailDto GetCommunityEmail(@RequestParam("city") String city) {
		log.info("Get /communityEmail/city called");
		CommunityEmailDto emailListDto = null;
		try {
			emailListDto = personsInfosService.getCommunityEmail(city);

			if (emailListDto == null) {
				log.info("personne pour cette ville");
			} else {
				log.info("Requête ok, liste des mails bien récupéré ");
			}

		} catch (Exception e) {
			log.error("erreur lors de la récupération des email, erreur : " + e);
			e.printStackTrace();
		}

		return emailListDto;
	}

}
