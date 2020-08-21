package com.example.examapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.examapp.model.UserModel;
import com.example.examapp.repository.UserModelRepo;

@Service
public class UserService {
	
	@Autowired
	private UserModelRepo userRepo;
	
    public UserModel findUserEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public UserModel findById(int id) {		
		return userRepo.findById(id);
	}

	public void saveUser(UserModel userExists) {
		userRepo.save(userExists);		
	}
}
