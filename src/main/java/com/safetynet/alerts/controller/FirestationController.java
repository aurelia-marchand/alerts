package com.safetynet.alerts.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.safetynet.alerts.exceptions.DonneeDejaExistanteException;
import com.safetynet.alerts.exceptions.DonneeIntrouvableException;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationServiceI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
public class FirestationController {

	@Autowired
	FirestationServiceI firestationService;

	/**
	 * Post - add firestation
	 * 
	 * @return url new firestation
	 */
	@PostMapping(value = "/firestation")
	public ResponseEntity<Void> postFirestation(@Valid @RequestBody Firestation firestationToPost) {
		log.info("POST /firestation called");
		String address = firestationToPost.getAddress();

		Firestation firestation = firestationService.getFirestation(address);

		if (firestation != null) {
			log.error("error Firestation déjà existante");
			throw new DonneeDejaExistanteException("La firestation que vous voulez créer existe déjà !");
		} else {
			firestationService.postFirestation(firestationToPost);
			// création de l'url pour la redirection vers la firestation modifiée
			URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/firestation")
					.queryParam("address", address).buildAndExpand(firestationToPost.getAddress()).toUri();
			log.info("uri created = " + location);
			return ResponseEntity.created(location).build();
		}

	}

	/**
	 * Put - Update a mapping of firestation
	 * 
	 * @return url firestation update
	 * 
	 */
	@PutMapping(path = "/firestation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> putFirestation(@Valid @RequestBody Firestation firestationToPut) {

		log.info("PUT /firestation called");
		URI location = null;
		Firestation firestation = firestationService.getFirestation(firestationToPut.getAddress());

		if (firestation == null) {
			log.error("Firestation introuvable");
			throw new DonneeIntrouvableException("La firestation demandée n'existe pas");
		} else {
			firestationService.putFirestation(firestationToPut);
			log.info("Requête ok, " + firestation + " a bien été modifiée");
			// création de l'url pour la redirection vers la firestation modifiée
			location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/firestation")
					.queryParam("address", firestationToPut.getAddress()).buildAndExpand(firestationToPut.getAddress())
					.toUri();
			log.info("uri created = " + location);
		}

		return ResponseEntity.created(location).build();
	}

	/**
	 * Delete - Delete a Firestation
	 * 
	 */
	@DeleteMapping("/firestation")
	public void deleteFirestation(@Valid @RequestBody Firestation firestationToDelete) {
		log.info("DELETE /firestation called");

		String address = firestationToDelete.getAddress();
		
		Firestation firestation = firestationService.getFirestation(address);

		if (firestation == null) {
			log.error("firestation introuvable");
			throw new DonneeIntrouvableException("La firestation demandée n'existe pas");
		} else {
			firestationService.deleteFirestation(address);
			log.info("Requête ok, " + firestation + " a bien été supprimé");
		}
	}

}
