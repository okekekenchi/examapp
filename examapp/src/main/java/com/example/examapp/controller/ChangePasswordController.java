package com.example.examapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.model.ChangePasswordModel;
import com.example.examapp.model.UserModel;
import com.example.examapp.service.UserService;

@RestController
public class ChangePasswordController {

	@Autowired private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping(value="/changepassword")
	public ModelAndView getChangePassword(ModelAndView mv){
		mv.addObject("changePasswordModel", new ChangePasswordModel());

		return mv;
	}
	
	@PostMapping(value="/changepassword")
	public ModelAndView ChangePassword(ModelAndView mv, ChangePasswordModel changePasswordModel){
		
		String Username = SecurityContextHolder.getContext().getAuthentication().getName();
		UserModel userExists = null;
		
		if(Username != null) {
			userExists = userService.findUserEmail(Username);	
		}else {
			mv.setViewName("/login");
		}
		
		if(userExists != null) {
			
			if(bCryptPasswordEncoder.matches(changePasswordModel.getCurrentPassword(), userExists.getPassword())) {
	
				userExists.setPassword(bCryptPasswordEncoder.encode(changePasswordModel.getNewPassword()));
				userService.saveUser(userExists);
				SecurityContextHolder.getContext().setAuthentication(null);
				mv.setViewName("/login");
			}else {
				mv.addObject("changePasswordModel", new ChangePasswordModel());
				mv.setViewName("/changepassword");
			}
		}else {
			mv.setViewName("/login");
		}		

		return mv;
	}
}