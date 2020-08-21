package com.example.examapp.controller;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginForm {

	@NotNull
    @Size(min = 2, max = 30)
	private String username;
	
	@NotNull
    @Min(5)
	private String password;

}
