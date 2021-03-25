package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.dto.FireListDto;
import com.safetynet.alerts.dto.FloodStationsDto;
import com.safetynet.alerts.dto.PeopleByAddressDto;
import com.safetynet.alerts.dto.PeopleCoveredDto;
import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.dto.PhoneAlertDto;

public interface PersonsInfosServiceI {

	public PeopleCoveredDto getListPersonsByStationNumber(int station);

	public ChildAlertDto getListChildrenByAddress(String address);

	public PhoneAlertDto getListPhoneByStation(int station);

	public FireListDto getPeopleByAddress(String address);

	public List<FloodStationsDto> getPeopleByListStation(List<Integer> stations);

	public PersonInfoDto getPersonInfo(String firstName, String lastName);
}
