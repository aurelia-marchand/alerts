package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.AlertPhoneDto;
import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.dto.CommunityEmailDto;
import com.safetynet.alerts.dto.DistrictDto;
import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.dto.StationsDto;
import com.safetynet.alerts.dto.StreetDto;

public interface PersonsInfosServiceI {

	public DistrictDto getListPersonsByStationNumber(int station);

	public ChildAlertDto getListChildrenByAddress(String address);

	public AlertPhoneDto getListPhoneByStation(int station);

	public StreetDto getPeopleByAddress(String address);

	public List<StationsDto> getPeopleByListStation(List<Integer> stations);

	public PersonInfoDto getPersonInfo(String firstName, String lastName);

	public CommunityEmailDto getCommunityEmail(String city);
}
