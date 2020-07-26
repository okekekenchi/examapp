package com.example.examapp.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.examapp.model.RoleModel;
import com.example.examapp.repository.RoleModelRepo;

@Service
public class RoleService {

	@Autowired
	private RoleModelRepo roleRepo;
	
	public RoleModel findByRoleName(String role) {
		return roleRepo.findByRoleName(role);
	}
	
	public void saveRole(RoleModel roleModel) {
		roleRepo.save(roleModel);
	}
	
	public String dataTableQuery(HttpServletRequest request) {
		String query = "";
		query += "SELECT * FROM RoleModel ";	
		
		if(request.getParameter("search[value]").length() > 0)
		{
			query += "WHERE roleId LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR roleName LIKE '%" + request.getParameter("search[value]") +"%' ";
		}			
		query += "ORDER BY "+request.getParameter("columns["+ request.getParameter("order[0][column]") +"][name]")
		+ " " + request.getParameter("order[0][dir]")+" ";
		
		if(Integer.parseInt(request.getParameter("length")) !=-1)
		{
			query += "LIMIT " + request.getParameter("start") + ", " + request.getParameter("length");
		}
		return query;
	}

	public void deleteCourse(int roleId) {
		roleRepo.deleteById(roleId);
	}
	
	public RoleModel findById(int roleId) {
		return roleRepo.findById(roleId);
	}
	
	public RoleModel roleExists(String role) {
		return roleRepo.findByRoleName(role);
	}
}
