package com.example.examapp.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "EmployeeModel")
@PrimaryKeyJoinColumn(name="employeeId")
public class EmployeeModel	extends UserModel {	

}