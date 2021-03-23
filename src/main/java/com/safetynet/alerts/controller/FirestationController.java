package com.safetynet.alerts.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationServiceI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FirestationController {

	@Autowired
	FirestationServiceI firestationService;
	
	/**
	 * Read all firestations-
	 * 
	 * @return list of firestations
	 */
	@GetMapping("/firestations")
	public List<Firestation> GetFirestations() {
		log.info("GET /firestations called");
		List<Firestation> firestations = firestationService.GetListFirestations();
		log.info("result : "+ firestations);
		return firestations;
	}

	/**
	 * Read - Get one firestation
	 * 
	 * @return A firestation
	 */
	@GetMapping("/firestation/get/{address}")
	public Firestation getFirestation(@PathVariable("address") String address) {
		log.info("GET /firestation/{"+ address +"}} called");

		Firestation firestation = firestationService.getFirestation(address);
		log.debug("firestation : " + firestation);
		if (firestation == null) {
			log.error("firestation introuvable");
			//throw new FirestationIntrouvableException("La firestation demandée n'existe pas");
		}
			
		return firestation;
	}

	/**
	 * Delete - Delete a Firestation
	 * 
	 */
	@DeleteMapping("/firestation/{address}")
	public void deleteFirestation(@PathVariable("address") String address) {
		log.info("DELETE /firestation/{adresse} called");

		Firestation firestation = firestationService.getFirestation(address);

		if (firestation == null) {
			log.error("firestation introuvable");
			//throw new FirestationIntrouvableException("La firestation demandée n'existe pas");
		} else {
			firestationService.deleteFirestation(address);
			log.info("Requête ok, " + firestation + " a bien été supprimé");
		}
	}

	/**
	 * Put - Update a mapping of firestation
	 * 
	 * @return url firestation update
	 * 
	 */
	@PutMapping(path = "/firestation/{address}", consumes=MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> putFirestation(@PathVariable("address") String address, @RequestBody Firestation firestationToPut) {

		log.info("PUT /firestation/"+address+" called");
		URI location = null;
		Firestation firestation = firestationService.getFirestation(address);

		if (firestation == null) {
			log.error("Firestation introuvable");
			//throw new FirestationIntrouvableException("La firestation demandée n'existe pas");
		} 
		else {
			firestationToPut.setAddress(address);
			firestationService.putFirestation(firestationToPut);
			log.info("Requête ok, " + firestation +" a bien été modifiée");
			// création de l'url pour la redirection vers la firestation modifiée
			location = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/firestation/{address}")
					.buildAndExpand(address).toUri();
		}
		
					return ResponseEntity.created(location).build();
	}

	/**
	 * Post - add firestation
	 * 
	 * @return url new firestation
	 */
	@PostMapping(value = "/firestation")
	public ResponseEntity<Void> postFirestation(@RequestBody Firestation firestationToPost) {
		log.info("POST /firestation called");
		String address = firestationToPost.getAddress();
		

		Firestation firestation = firestationService.getFirestation(address);
		if (firestation != null) {
			log.error("Firestation déjà existante");
			//throw new FirestationDejaExistanteException("La firestation que vous voulez créer existe déjà !");
		} else {
			firestationService.postFirestation(firestationToPost);
		}
		// création de l'url pour la redirection vers la firestation modifiée
		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/firestation/{address}")
				.buildAndExpand(firestationToPost.getAddress())
				.toUri();
		log.info("uri = " + location);
		return ResponseEntity.created(location).build();
	}
}
