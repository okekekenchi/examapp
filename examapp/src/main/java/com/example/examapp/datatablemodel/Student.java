package com.example.examapp.datatablemodel;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student extends User{
	
	private String courseModel;
	private  int takenTest;
	private String validStatus, logout, delete, view, address, phone, gender, onlineStatus;

	public Student(int userId, String firstName, String lastName, String otherName, String email,
			int status, int online, String courseModel, String image) {		
		super(userId, firstName, lastName, otherName, email, status, online, image);		
		this.courseModel = courseModel;	
	}

	public Student(int userId, String firstName, String lastName, String otherName, String email,
			String address, String phone, String gender, int Status, int online, Date regdate,
			String profileImage,  String courseModel) {
		super(userId, firstName, lastName, otherName, email, Status, online,
				regdate, profileImage);
		this.courseModel = courseModel;
		this.phone = phone;
		this.gender = gender;
		this.address = address;
	}	
	
}