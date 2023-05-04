package com.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.model.Admin;
import com.application.repository.AdminRegistrationRepository;

@Service
public class AdminRegistrationService {
	
	@Autowired
	private AdminRegistrationRepository adminRegistrationRepo;
	
	
	public Admin fetchAdminByEmailAndPassword(String email,String password)
	{
		return adminRegistrationRepo.findByEmailAndPassword(email, password);
	}

}
