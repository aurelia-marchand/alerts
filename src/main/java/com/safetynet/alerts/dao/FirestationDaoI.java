package com.safetynet.alerts.dao;

import java.util.List;

import com.safetynet.alerts.model.Firestation;

public interface FirestationDaoI {

	List<Firestation> findAllFirestations();

	Firestation getFirestation(String address);

	Firestation postFirestation(Firestation firestationToPost);

	Firestation putFirestation(Firestation firestation);

	Firestation deleteFirestation(String address);

}
