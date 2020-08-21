package com.example.examapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.RegistrationCardModel;

@Repository
public interface VoucherModelRepo extends JpaRepository<RegistrationCardModel, Long>{

	static final String GET_ACTIVE_VOUCHERS =
			"SELECT COUNT(voucher) FROM RegistrationCardModel voucher WHERE voucher.active = 0";
	@Query(value = GET_ACTIVE_VOUCHERS)
	int nVouchersUsed();
}
