package com.safetynet.alerts.dao;

import java.util.List;

import com.safetynet.alerts.model.Firestation;

public interface FirestationDaoI {

	List<Firestation> findAllFirestations();

	Firestation findFirestationByAddress(String address);

	Firestation saveFirestation(Firestation firestationToPost);

	Firestation updateFirestation(Firestation firestation);

	Firestation deleteFirestationByAddress(String address);

}
