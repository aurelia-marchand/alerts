package com.safetynet.alerts.dao;

import com.safetynet.alerts.model.PersonsInfos;

public interface AccessJsonI {

	public PersonsInfos getData();
	
	public PersonsInfos writeData(PersonsInfos personsInfos);
	
	public PersonsInfos updateData();
	
	public PersonsInfos deleteData();
	
}

