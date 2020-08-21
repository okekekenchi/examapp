package com.example.examapp.datatablemodel;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee extends User{
	
	private String validStatus;
	private String logout;
	private String delete;
	private String view;
	private String onlineStatus;

	public Employee(Integer userId, String firstName, String lastName, String otherName, String email,
			int Status, int online, String image) {
		
		super(userId, firstName, lastName, otherName, email, Status, online, image);
	}
	
	public Employee(int userId, String firstName, String lastName, String otherName, String email,
			int Status, int online, Date regdate, String profileImage) {
		
		super(userId, firstName, lastName, otherName, email, Status, online,
				regdate, profileImage);	
	}	
}
