package com.example.examapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.examapp.model.UserDetailModel;
import com.example.examapp.model.UserModel;
import com.example.examapp.repository.UserModelRepo;

@Service
public class UserDetailService implements UserDetailsService{
	
	  @Autowired 
	  UserModelRepo userRepo;
	  
	  @Override public UserDetails loadUserByUsername(String email) throws
	  UsernameNotFoundException {
		  UserModel user =  userRepo.findByEmail(email);
		  return new UserDetailModel(user);
	  }
}