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

	@Column(name="nComputers", length=9)
	private int nComputers;
	
	@Column(name="examStartDate")
	private Date examStartDate;
	
	@Column(name="examStartTime", length=7)
	private String examStartTime;
	
	@Column(name="examEndTime", length=7)
	private String examEndTime;
	
	@Column(name="sessionTime", length=5)
	private int sessionTime; 
	
	@Column(name="enableStudent", length=25)
	private int enableStudentReg = 0;
	
	@Column(name="enableEmployee", length=1)
	private int enableEmployeeReg = 1;
	
	@Column(name="enableRoleReg", length=1)
	private int enableRoleReg = 1;
	
	@Column(name="enableExam", length=1)
	private int enableExam = 0;
	
	@Column(name="enableCourse", length=1)
	private int enableCourseReg = 0;
	
	@Column(name="enableSubject", length=1)
	private int enableSubjectReg = 0;
	
	@Column(name="numberOfQuestion", length=3)
	private int numberOfQuestion = 0;
}
