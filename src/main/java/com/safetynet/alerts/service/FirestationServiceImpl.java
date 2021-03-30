package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dao.FirestationDaoI;
import com.safetynet.alerts.model.Firestation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FirestationServiceImpl implements FirestationServiceI {

	@Autowired
	private FirestationDaoI firestationDao;

	@Override
	public Firestation getFirestation(String address) {
		log.debug("service demande au dao recherche station à l'adresse : " + address);
		Firestation firestation = firestationDao.findFirestationByAddress(address);
		return firestation;
	}

	@Override
	public Firestation postFirestation(Firestation firestation) {
		String address = firestation.getAddress();
		
		Firestation firestationToPost = firestationDao.findFirestationByAddress(address);

		if (firestationToPost == null) {
			log.debug("envoi de la firestation à créer au dao");
			firestationDao.saveFirestation(firestation);
		} else {
			log.error("Firestation déjà existante");
		}

		return firestation;
	}

	@Override
	public Firestation putFirestation(Firestation firestationToPut) {
		Firestation firestation = firestationDao.findFirestationByAddress(firestationToPut.getAddress());

		// Mise à jour de la personne selon les valeurs reçues
		int station = firestationToPut.getStation();
		if (station != 0) {
			log.debug("reçu valeur station :" + station);
			firestation.setStation(station);
		}

		firestationDao.updateFirestation(firestation);
		return firestationToPut;
	}

	@Override
	public Firestation deleteFirestation(String address) {
		firestationDao.findFirestationByAddress(address);

		log.debug("envoi de la firestation à supprimer au dao");
		firestationDao.deleteFirestationByAddress(address);

		return null;
	}

	@Override
	public List<Firestation> GetListFirestations() {
		log.debug("étape service get list firestations");
		return firestationDao.findAllFirestations();
	}

}
