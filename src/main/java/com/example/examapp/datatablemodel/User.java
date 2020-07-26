package com.example.examapp.datatablemodel;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
	
	private int userId;
	private String firstName;
	private String lastName;
	private String otherName;
	private String email;
	private String roleName;
	private String password;
	private int status;
	private int online;
	private Date regdate;
	private String profileImage;
	
	public User(int userId, String firstName, String lastName, String otherName, String email, int status,
			int online, String image) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.otherName = otherName;
		this.email = email;
		this.status = status;
		this.online = online;
		this.profileImage = image;
	}

	public User(int userId, String firstName, String lastName, String otherName, String email,
			int status, int online, Date regdate, String profileImage) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.otherName = otherName;
		this.email = email;
		this.status = status;
		this.online = online;
		this.regdate = regdate;
		this.profileImage = profileImage;
	}
}