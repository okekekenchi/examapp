package com.example.examapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)

@Table(name = "RoleModel")
public class RoleModel {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="roleId")
	private int roleId;
	
	@Column(name="roleName", length=25)
	@NotEmpty(message = "*Please provide a valid role")
	private String roleName;
	
	@Column(name = "roleStatus", nullable=false, length=1)
	private Integer roleStatus;
}

