package com.example.examapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.RoleModel;

@Repository
public interface RoleModelRepo extends JpaRepository<RoleModel, Integer>{
	RoleModel findByRoleName(String role);
	RoleModel findById(int roleId);
}
