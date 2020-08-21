package com.example.examapp.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.examapp.repository.VoucherModelRepo;

@Service
public class VoucherService {

	@Autowired
	private VoucherModelRepo voucherRepo;
	
	public int getnVouchersUsed() {
		
		return voucherRepo.nVouchersUsed();
	}
	
	public int getnVouchers() {
		
		return (int)voucherRepo.count();
	}
	
	public String dataTableQuery(HttpServletRequest request) {
		String query = "";
		
		query += "SELECT u.userId, u.firstName, u.lastName, u.otherName, count(serialNumber) as 'sales' "+	
		 			"FROM registrationcardmodel r  "+
		 			"INNER JOIN vendormodel v ON r.vendorId = v.vendorId " + 
		 			"INNER JOIN usermodel u ON v.vendorId = u.userId "+
		 			"GROUP BY r.vendorId";		
		
		return query;
	}
}