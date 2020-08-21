package com.example.examapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
	
	@Column(name="pinNumber")
	@NotNull
	private String pinNumber;
	
	@Column(name="email")
	@NotNull
	private String email;
	
	@Column(name = "active")
	private Integer active;
	
}
