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
	private int nComputers;
	
	@Column(name="examStartDate")
	private Date examStartDate;
	
	@Column(name="examStartTime")
	private String examStartTime;
	
	@Column(name="examEndTime")
	private String examEndTime;
	
	@Column(name="sessionTime")
	private int sessionTime; 
	
	@Column(name="enableStudent")
	private int enableStudentReg = 0;
	
	@Column(name="enableEmployee")
	private int enableEmployeeReg = 1;
	
	@Column(name="enableRoleReg")
	private int enableRoleReg = 1;
	
	@Column(name="enableExam")
	private int enableExam = 0;
	
	@Column(name="enableCourse")
	private int enableCourseReg = 0;
	
	@Column(name="enableSubject")
	private int enableSubjectReg = 0;
	
	@Column(name="numberOfQuestion")
	private int numberOfQuestion = 0;
}
