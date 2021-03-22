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
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordServiceI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MedicalRecordController {
	
	@Autowired
	MedicalRecordServiceI medicalRecordService;
	
	/**
	 * Read all medical records
	 * 
	 * @return - A list of medical records
	 */
	@GetMapping("/medicalRecords")
	public List<MedicalRecord> getMedicalRecords() {
		log.info("GET /medicalRecords called");
		List<MedicalRecord> medicalRecords = medicalRecordService.getListMedicalRecords();
		log.info("result : " + medicalRecords);
		
		return medicalRecords;
		
	}
	
	/**
	 * Get one medical record
	 * 
	 * @return - medical record for one person
	 */
	@GetMapping("/medicalRecord/{firstName}/{lastName}")
	public MedicalRecord getMedicalRecord(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		
		log.info("GET /medicalRecord/{"+ firstName +"}/{"+lastName+"} called");

		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(firstName, lastName);
		
		if (medicalRecord == null) {
			log.error("dossier introuvable");
			//throw new MedicalRecordIntrouvableException("Le dossier demandé n'existe pas");
		}
			
		return medicalRecord;
		
	}
	
	/**
	 * Delete - Delete a medical record
	 * 
	 */
	@DeleteMapping("/medicalRecord/{firstName}/{lastName}")
	public void deleteMedicalRecord(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		log.info("DELETE /medicalRecord/{firstName}/{lastName} called");

		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(firstName, lastName);

		if (medicalRecord == null) {
			log.error("dossier introuvable");
			//throw new MedicalRecordIntrouvableException("Le dossier demandé n'existe pas");
		} else {
			medicalRecordService.deleteMedicalRecord(firstName, lastName);
			log.info("Requête ok, le dossier de " + firstName + " " + lastName + " a bien été supprimé");
		}
	} 
	
	/**
	 * Put - update a medical record
	 * 
	 * @return -url medical record update
	 */
	@PutMapping("/medicalRecord/{firstName}/{lastName}")
	public ResponseEntity<Void> putMedicalRecord(@PathVariable("firstName") String firstName,
			@PathVariable("lastName") String lastName, @RequestBody MedicalRecord medicalRecordToPut) {
				
		
		log.info("PUT /medicalRecord/"+firstName+"/+"+lastName+" called");

		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(firstName, lastName);

		if (medicalRecord == null) {
			log.error("Dossier introuvable");
			//throw new MedicalRecordIntrouvableException("Le dossier demandé n'existe pas");
		} 
		else {
			medicalRecordToPut.setFirstName(firstName);
			medicalRecordToPut.setLastName(lastName);
			medicalRecordService.putMedicalRecord(medicalRecordToPut);
			log.info("Requête ok, le dossier de " + firstName + " " + lastName + " a bien été modifié");
			
			
			
		}
		// création de l'url pour la redirection vers le dossier médical modifié
					Map<String, String> params = new HashMap<String, String>();
					params.put("firstName", firstName);
					params.put("lastName", lastName);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/medicalRecord/{firstName}/{lastName}")
				.buildAndExpand(params).toUri();
		return ResponseEntity.created(location).build();
	}
	
	/**
	 * Post - add medical record
	 * 
	 * @return - url medical record
	 */
	@PostMapping("/medicalRecord")
	public ResponseEntity<Void> postMedicalRecord(@RequestBody MedicalRecord medicalRecordToPost) {
		log.info("POST /medicalRecord called");
		String firstName = medicalRecordToPost.getFirstName();
		String lastName = medicalRecordToPost.getLastName();
		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(firstName, lastName);
		if (medicalRecord != null) {
			log.error("medical record déjà existant");
			//throw new MedicalRecordDejaExistantException("Le dossier que vous voulez créer existe déjà !");
		} else {
			medicalRecordService.postMedicalRecord(medicalRecordToPost);
		}
		// création de l'url pour la redirection vers le dossier crée
					Map<String, String> params = new HashMap<String, String>();
					params.put("firstName", firstName);
					params.put("lastName", lastName);

					URI location = ServletUriComponentsBuilder
							.fromCurrentContextPath()
							.path("/medicalRecord/{firstName}/{lastName}")
							.buildAndExpand(params).toUri();

					log.info("uri = " + location);

					return ResponseEntity.created(location).build();
	}

}
