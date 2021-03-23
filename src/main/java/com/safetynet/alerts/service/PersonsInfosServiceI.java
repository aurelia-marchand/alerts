package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.dto.PeopleCoveredDto;

public interface PersonsInfosServiceI {

	public PeopleCoveredDto getListPersonsByStationNumber(int station);

	public ChildAlertDto getListChildrenByAddress(String address);
}
