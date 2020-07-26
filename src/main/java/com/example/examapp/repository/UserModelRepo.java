package com.example.examapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.UserModel;

@Repository
public interface UserModelRepo extends JpaRepository <UserModel, Integer>{
	
	UserModel findByEmail(String email);
	UserModel findById(int userId);
}
