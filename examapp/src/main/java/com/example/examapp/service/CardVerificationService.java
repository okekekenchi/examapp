package com.example.examapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.examapp.model.RegistrationCardModel;
import com.example.examapp.repository.CardVerificationRepo;

@Service
public class CardVerificationService {

	@Autowired CardVerificationRepo cvRepo;
	
	public RegistrationCardModel findBySerialNumber(long serialNumber) {
		return cvRepo.findBySerialNumber(serialNumber);
	}
	public RegistrationCardModel findByPinNumber(String pin) {
		return cvRepo.findByPinNumber(pin);
	}
	
	public void save(RegistrationCardModel registrationCardModel) {
		cvRepo.save(registrationCardModel);
	}
}
