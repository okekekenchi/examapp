package com.example.examapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

	private Integer id = 0;
	private String email = "";
	private String firstName = "";
	private String message = "";
	private int examTime = 0;
}
