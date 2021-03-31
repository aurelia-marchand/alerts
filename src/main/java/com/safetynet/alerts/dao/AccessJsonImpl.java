package com.safetynet.alerts.dao;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnProperty(name = "app.environment", havingValue = "dev")
@Slf4j
public class AccessJsonImpl implements AccessJsonI{

	ObjectMapper mapper = new ObjectMapper();
	
	File newState = new File("src/main/resources/data.json");
	
	@Autowired
	Datas personsInfos;
	
	@Override
	public Datas getData() {
		try {
			personsInfos = mapper.readValue(newState, Datas.class);
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
	public Datas writeData(Datas personsInfos) {
		try {
			mapper.writeValue(newState, personsInfos);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Datas updateData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Datas deleteData() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
