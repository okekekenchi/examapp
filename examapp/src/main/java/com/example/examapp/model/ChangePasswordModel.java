package com.example.examapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ChangePasswordModel {
	private String currentPassword;
	private String newPassword;
	private String confirmPassword;
}
