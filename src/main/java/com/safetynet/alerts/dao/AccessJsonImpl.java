package com.safetynet.alerts.dao;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.PersonsInfos;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AccessJsonImpl implements AccessJsonI{

	ObjectMapper mapper = new ObjectMapper();
	
	File newState = new File("src/main/resources/data.json");
	
	@Autowired
	PersonsInfos personsInfos;
	
	@Override
	public PersonsInfos getData() {
		try {
			personsInfos = mapper.readValue(newState, PersonsInfos.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return personsInfos;
	}

	@Override
	public PersonsInfos writeData() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
