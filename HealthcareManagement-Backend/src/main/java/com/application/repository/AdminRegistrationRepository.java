package com.application.repository;

import org.springframework.data.repository.CrudRepository;

import com.application.model.Admin;

public interface AdminRegistrationRepository extends CrudRepository<Admin, String>{
	
	
	public Admin findByEmailAndPassword(String email, String password);

}
