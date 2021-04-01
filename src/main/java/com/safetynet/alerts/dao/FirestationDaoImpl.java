package com.safetynet.alerts.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.safetynet.alerts.model.Firestation;

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
		
			firestations = accessJson.getData().getFirestations();
			return firestations;
		
	}

	@Override
	public Firestation findFirestationByAddress(String address) {
		List<Firestation> firestations = accessJson.getData().getFirestations();
		Firestation firestationToGet = null;
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
		Datas personsInfos = accessJson.getData();
		List<Firestation> firestations = findAllFirestations();
		Firestation newFirestation = firestationToPost;
		
		firestations.add(newFirestation);
		personsInfos.setFirestations(firestations);

		accessJson.writeData(personsInfos);
		return newFirestation;

	}

	@Override
	public Firestation updateFirestation(Firestation firestationToPut) {
		Datas personsInfos = accessJson.getData();
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
		Datas personsInfos = accessJson.getData();
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
