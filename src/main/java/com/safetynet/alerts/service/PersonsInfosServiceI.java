package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PeopleCoveredDto;

public interface PersonsInfosServiceI {

	public PeopleCoveredDto GetListPersonsByStationNumber(int station);
}
