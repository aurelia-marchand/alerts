package com.safetynet.alerts.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.PersonsInfos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
public class FirestationDaoImpl implements FirestationDaoI {

	@Autowired
	AccessJsonI accessJson;
	
	@Override
	public List<Firestation> findAllFirestations() {
		List<Firestation> firestations;
		try {
			firestations = accessJson.getData().getFirestations();
			return firestations;
		} catch (Exception e) {
			log.error("erreur pendant la récupération de la liste de firestations : " + e);	
		}
		return null;
	}

	@Override
	public Firestation findFirestationByAddress(String address) {
		List<Firestation> firestations = accessJson.getData().getFirestations();
		Firestation firestationToGet = new Firestation();
		for (Firestation firestation : firestations) {
			if (firestation.getAddress().equalsIgnoreCase(address)) {
				log.debug("dao renvoi : "+ firestation);
				firestationToGet = firestation;
			}
		}
		return firestationToGet;
	}

	@Override
	public Firestation saveFirestation(Firestation firestationToPost) {
		PersonsInfos personsInfos = accessJson.getData();
		List<Firestation> firestations = findAllFirestations();
		Firestation newFirestation = firestationToPost;
		
		firestations.add(newFirestation);
		personsInfos.setFirestations(firestations);

		accessJson.writeData(personsInfos);
		return newFirestation;

	}

	@Override
	public Firestation updateFirestation(Firestation firestationToPut) {
		PersonsInfos personsInfos = accessJson.getData();
		List<Firestation> firestations = findAllFirestations();
		Firestation firestationToUpdate = firestationToPut;
		int index = 0;
		for (Firestation firestation : firestations) {
			if (firestation.getAddress().equalsIgnoreCase(firestationToUpdate.getAddress())) {
				firestationToUpdate = firestation;
				index = firestations.indexOf(firestation);
				log.debug("index récupéré pour update");
			}
		}
	firestations.set(index, firestationToPut);
	personsInfos.setFirestations(firestations);
	accessJson.writeData(personsInfos);
	return firestationToUpdate;

	}

	@Override
	public Firestation deleteFirestationByAddress(String address) {
		PersonsInfos personsInfos = accessJson.getData();
		List<Firestation> firestations = findAllFirestations();
		Firestation firestationToDelete = new Firestation();
		
		
		for (Firestation firestation : firestations) {
			if (firestation.getAddress().equalsIgnoreCase(address)) {
				firestationToDelete = firestation;
				log.debug("firestation a supprimer trouvée :  "+ firestationToDelete.toString());
			}
		}
		firestations.remove(firestationToDelete);

		personsInfos.setFirestations(firestations);

		accessJson.writeData(personsInfos);
		return null;

	}

}
