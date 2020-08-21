package com.example.examapp.datatablemodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Role {

	private int roleId;
	private String roleName;
	
	private String validStatus;
	private String update;
	private String delete;
	
	public Role(int roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}	
}