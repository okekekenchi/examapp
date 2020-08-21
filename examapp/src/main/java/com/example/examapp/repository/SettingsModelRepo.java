package com.example.examapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.SettingsModel;

@Repository
public interface SettingsModelRepo extends JpaRepository <SettingsModel, Integer>{

	SettingsModel findById(int settingsId);
}