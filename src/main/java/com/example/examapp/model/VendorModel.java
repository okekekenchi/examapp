package com.example.examapp.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "VendorModel")
@PrimaryKeyJoinColumn(name="vendorId")
public class VendorModel extends UserModel {

	@OneToMany(mappedBy="vendorModel")
	private Set<RegistrationCardModel> registrationCardModel = new HashSet<>();
}