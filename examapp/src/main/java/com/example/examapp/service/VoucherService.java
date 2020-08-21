package com.example.examapp.service;

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
}