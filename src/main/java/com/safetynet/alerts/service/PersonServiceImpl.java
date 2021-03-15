package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dao.PersonDaoI;
import com.safetynet.alerts.model.Person;

@Service
public class PersonServiceImpl implements PersonServiceI{

	@Autowired
	private PersonDaoI personDao;
	
	@Override
	public List<Person> GetListPersons() {
		return personDao.findAllPersons();
	}
	
	@Override
	public Person getPerson(String firstName, String lastName) {
		return personDao.getPerson(firstName, lastName);
	}

	@Override
	public Person postPerson(String firstName, String lastName, String address, String city, int zip, String phone,
			String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person putPerson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person deletePerson(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

}
