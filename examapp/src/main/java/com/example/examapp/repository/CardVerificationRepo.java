package com.example.examapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.RegistrationCardModel;

@Repository
public interface CardVerificationRepo extends JpaRepository <RegistrationCardModel,  Long> {

	RegistrationCardModel findByPinNumber(String pinNumber);
	RegistrationCardModel findBySerialNumber(long serialNumber);
}
