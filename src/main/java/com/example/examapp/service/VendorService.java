package com.example.examapp.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.examapp.model.VendorModel;
import com.example.examapp.repository.VendorModelRepo;

@Service
public class VendorService {

	@Autowired
	private VendorModelRepo vendorRepo;
	
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PersistenceContext
	private EntityManager entityManager;
	
	Query query = null;
	
	String queryString = null;
	
	public int getVendorsOnline() {
		queryString= "SELECT * FROM examappdb.getVendorsOnline";
		query = entityManager.createNativeQuery(queryString);
		return Integer.valueOf(query.getResultList().get(0).toString());
	}
	
	public int getActiveVendors() {
		queryString= "SELECT * FROM examappdb.getActiveVendors";
		query = entityManager.createNativeQuery(queryString);
		return Integer.valueOf(query.getResultList().get(0).toString());
	}
             
    public VendorModel findVendorByEmail(String email) {
		return vendorRepo.findByEmail(email);
	}
    
	public void saveVendor(VendorModel vendor) {
		try {
			vendor.setPassword(bCryptPasswordEncoder.encode(vendor.getPassword()));
			vendor.setOnline(0);
	        vendorRepo.save(vendor);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateVendor(VendorModel vendor) {
		try {
			vendorRepo.save(vendor);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public List<VendorModel> getVendors() {
		return vendorRepo.findAll();
	}
	
	public String dataTableQuery(HttpServletRequest request) {
		String query = "";
		query += "SELECT DISTINCT v.vendorId, u.firstName, u.lastName, u.image, u.otherName, u.status, "+
				"u.email, r.roleName, u.online, "+
				"u.password, u.regdate "+
				"FROM UserModel u "+
				"INNER JOIN vendormodel v on u.userid = v.vendorid "+
				"INNER JOIN userrole ur on u.userid = ur.userId "+
				"INNER JOIN rolemodel r on ur.roleId = r.roleId ";
		
		if(request.getParameter("search[value]").length() > 0)
		{
			query += "WHERE v.vendorId LIKE '%" + request.getParameter("search[value]") + "%' ";
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

	public VendorModel findById(int vendorId) {
		return vendorRepo.findById(vendorId);
	}
}
