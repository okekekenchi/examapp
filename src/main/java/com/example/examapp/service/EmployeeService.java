package com.example.examapp.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.examapp.model.EmployeeModel;
import com.example.examapp.repository.EmployeeModelRepo;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeModelRepo empRepo;
	
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
             
    public EmployeeModel findEmployeeEmail(String email) {
		return empRepo.findByEmail(email);
	}
    
	public void saveEmployee(EmployeeModel emp) {
		try {
			emp.setPassword(bCryptPasswordEncoder.encode(emp.getPassword()));
	        emp.setOnline(0);
	        empRepo.save(emp);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateEmployee(EmployeeModel emp) {
		try {
	        empRepo.save(emp);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public List<EmployeeModel> getemployees() {
		return empRepo.findAll();
	}
	
	public String dataTableQuery(HttpServletRequest request) {
		String query = "";
		query += "SELECT DISTINCT e.employeeId, u.firstName, u.lastName, u.image, u.otherName, u.status, "+
				"u.email, r.roleName, u.online, "+
				"u.password, u.regdate "+
				"FROM UserModel u "+
				"INNER JOIN employeemodel e on u.userid = e.employeeid "+
				"INNER JOIN userrole ur on u.userid = ur.userId "+
				"INNER JOIN rolemodel r on ur.roleId = r.roleId ";
		
		if(request.getParameter("search[value]").length() > 0)
		{
			query += "WHERE e.employeeId LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR u.firstName LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR u.lastName LIKE '%" + request.getParameter("search[value]") +"%' ";
			query += "OR u.otherName LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR u.status LIKE '%" + request.getParameter("search[value]") +"%' ";
			query += "OR u.email LIKE '%" + request.getParameter("search[value]") +"%' ";
			query += "OR u.online LIKE '%" + request.getParameter("search[value]") +"%' ";
			query += "OR u.regdate LIKE '%" + request.getParameter("search[value]") + "%' ";
		}			
		query += "ORDER BY "+request.getParameter("columns["+ request.getParameter("order[0][column]") +"][name]")
		+ " " + request.getParameter("order[0][dir]")+" ";
		
		if(Integer.parseInt(request.getParameter("length")) !=-1)
		{
			query += "LIMIT " + request.getParameter("start") + ", " + request.getParameter("length");
		}
		return query;
	}

	public EmployeeModel findById(int empId) {
		return empRepo.findById(empId);
	}
}
