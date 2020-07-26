package com.example.examapp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SettingsModel")
public class SettingsModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="settingsId")
	private int settingsId;

	@Column(name="nComputers")
	private int nComputers; // Number of working computers available in the school
	
	@Column(name="examStartDate")
	private Date examStartDate; // When the exam will begin. The end date value will be auto generated based on this value
	
	@Column(name="examStartTime")
	private String examStartTime; // When the exam will begin. For example 07:30 AM
	
	@Column(name="examEndTime")
	private String examEndTime; // When the will end. For example 05:30 AM
	
	@Column(name="sessionTime")
	private int sessionTime; // Total time taken in minutes, to finish an online test.
	
	@Column(name="enableStudent")
	private int enableStudentReg = 1; // Enable student registration
	
	@Column(name="enableEmployee")
	private int enableEmployeeReg = 1; // Enable employee registration
	
	@Column(name="enableRoleReg")
	private int enableRoleReg = 1; // Enable role Registration
	
	@Column(name="enableExam")
	private int enableExam = 1; // Enable Online testing
	
	@Column(name="enableCourse")
	private int enableCourseReg = 1; // Enable course registration
	
	@Column(name="enableSubject")
	private int enableSubjectReg = 1; // Enable subject registration
}
