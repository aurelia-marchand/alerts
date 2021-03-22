package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.model.Firestation;

public interface FirestationServiceI {

	Firestation getFirestation(String address);
	
	Firestation postFirestation(Firestation firestation);

	Firestation putFirestation(Firestation firestation);

	Firestation deleteFirestation(String address);

	List<Firestation> GetListFirestations();

}
