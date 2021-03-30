package com.safetynet.alerts.dao;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.PersonsInfos;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnProperty(name = "app.environment", havingValue = "test")
@Slf4j
public class AccessJsonEnvDevImpl implements AccessJsonI{


		ObjectMapper mapper = new ObjectMapper();
		
		File newState = new File("src/main/resources/data-test.json");
		
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
		public PersonsInfos writeData(PersonsInfos personsInfos) {
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
		public PersonsInfos updateData() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PersonsInfos deleteData() {
			// TODO Auto-generated method stub
			return null;
		}
		
	

}
