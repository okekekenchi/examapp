package com.example.examapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.EmployeeModel;

@Repository
public interface EmployeeModelRepo extends JpaRepository<EmployeeModel, Integer>{
	EmployeeModel findByEmail(String email);
	EmployeeModel findById(int employeeId);
}