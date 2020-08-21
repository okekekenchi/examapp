package com.example.examapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.examapp.model.SettingsModel;
import com.example.examapp.repository.SettingsModelRepo;

@Service
public class SettingsService {
	
	@Autowired
	private SettingsModelRepo settingsModelRepo;
	
	public List<SettingsModel> getSettings() {
		return settingsModelRepo.findAll();
	}
	
	public SettingsModel getSettings(int settingsId) {
		return settingsModelRepo.findById(settingsId);
	}
	
	public SettingsModel getSetting() {
		return settingsModelRepo.findById(1);
	}

	public void Save(SettingsModel settingsModel) {
		settingsModelRepo.save(settingsModel);		
	}
}