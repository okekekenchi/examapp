package com.example.examapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "RegistrationCardModel")
public class RegistrationCardModel {
	
	@Id
	@Column(name="serialNumber")
	@NotNull
	private long serialNumber;
	
	@Column(name="pinNumber", length=20)
	@NotNull
	private String pinNumber;
	
	@ManyToOne
	@JoinColumn(name = "vendorId", nullable=false)
	private VendorModel vendorModel;
	
	@Column(name="email", length=150)
	@NotNull
	private String email;
	
	@Column(name = "active", length=1)
	private int  active;
	
}
