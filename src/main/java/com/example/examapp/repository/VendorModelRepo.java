package com.example.examapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.VendorModel;

@Repository
public interface VendorModelRepo extends JpaRepository<VendorModel, Integer>{
	VendorModel findByEmail(String email);
	VendorModel findById(int vendorId);
}