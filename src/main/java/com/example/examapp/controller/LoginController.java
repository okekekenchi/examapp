package com.example.examapp.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.model.AuthenticationRequest;
import com.example.examapp.model.AuthenticationResponse;
import com.example.examapp.model.StudentModel;
import com.example.examapp.model.UserModel;
import com.example.examapp.service.SettingsService;
import com.example.examapp.service.StudentService;
import com.example.examapp.service.SubjectService;
import com.example.examapp.service.UserDetailService;
import com.example.examapp.service.UserService;
import com.google.gson.Gson;

@RestController
public class LoginController {
	
	@Autowired private AuthenticationManager authenticationManager;	
	@Autowired UserDetailService userDetailService;
	@Autowired StudentService studentService;
	@Autowired SubjectService subjectService;
	@Autowired SettingsService settingsService;
	@Autowired UserService userService;

	@GetMapping(value="/login")
	public ModelAndView Login(ModelAndView mv) {	
		
		return mv;
	}
	
	@PostMapping(value="/login?error")
	public ModelAndView login(ModelAndView mv) {

		mv.addObject("loginError", true);
		return mv;
	}
	
	@PostMapping("/authenticate")
	@ResponseBody
	public String authenticateUser(@RequestBody AuthenticationRequest authRequest) throws Exception{
		/**
		 * Checks if the online exam has been enabled
		 */
		if(settingsService.getSettings().get(0).getEnableStudentReg() == 1) {
			return new Gson().toJson(new AuthenticationResponse(0, "", "", "Online exam disabled", 0));
		}
		
		boolean badCredential = false;
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
					);
		}catch(BadCredentialsException ex) {
			badCredential = true;
		}
		
		int examTimeFormat = settingsService.getSetting().getSessionTime();

		if(!badCredential) {
			StudentModel studentExists = studentService.findStudentEmail(authRequest.getUsername());
			if(studentExists != null && studentExists.getTakenTest() == 0 && studentExists.getOnline() == 0) {

				studentExists.setOnline(1);
				studentExists.setStatus(1);
				//studentService.updateStudent(studentExists);
				return new Gson().toJson(new AuthenticationResponse(studentExists.getUserId(), studentExists.getEmail(),
											studentExists.getFirstName(), "successful", examTimeFormat));
			}else {
				return new Gson().toJson(new AuthenticationResponse(0, "", "", "unauthorised", examTimeFormat));
			}
		}else {
			return new Gson().toJson(new AuthenticationResponse(0, "", "", "unsuccessful", examTimeFormat));
		}
	}
}