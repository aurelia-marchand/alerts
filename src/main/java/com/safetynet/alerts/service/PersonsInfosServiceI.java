package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.model.PhoneAlert;
import com.safetynet.alerts.model.ChildAlert;
import com.safetynet.alerts.model.CommunityEmail;
import com.safetynet.alerts.model.DistrictPeople;
import com.safetynet.alerts.model.StationsPeople;
import com.safetynet.alerts.model.StreetPeople;

public interface PersonsInfosServiceI {

	public DistrictPeople getListPersonsByStationNumber(int station);

	public ChildAlert getListChildrenByAddress(String address);

	public PhoneAlert getListPhoneByStation(int station);

	public StreetPeople getPeopleByAddress(String address);

	public List<StationsPeople> getPeopleByListStation(List<Integer> stations);

	public List<PersonInfoDto> getPersonInfo(String firstName, String lastName);

	public CommunityEmail getCommunityEmail(String city);
}
