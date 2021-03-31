package com.safetynet.alerts.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.safetynet.alerts.dto.StationsPeopleByAddressDto;

import lombok.Data;

@Data
@Component
public class StationsPeople {

	List<StationsPeopleByAddressDto> stationsPeopleByAddressDto;
}
