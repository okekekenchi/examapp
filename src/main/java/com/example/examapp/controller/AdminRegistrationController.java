package com.example.examapp.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.model.EmployeeModel;
import com.example.examapp.model.RoleModel;
import com.example.examapp.model.UserModel;
import com.example.examapp.service.EmployeeService;
import com.example.examapp.service.RoleService;
import com.example.examapp.service.SettingsService;
import com.example.examapp.service.UserService;

@RestController
public class AdminRegistrationController {
	
	@Autowired EmployeeService empService;
	
	@Autowired SettingsService settingsService;

	@Autowired UserService userService;
	
	@Autowired RoleService roleService;

	@GetMapping(value="/adminregistration")
	public ModelAndView registration(ModelAndView mv){
		
		if(roleService.roleExists("ADMIN") != null) {
			
			mv.addObject("employeeModel", new EmployeeModel());
		}else {
			mv.setViewName("disablederrorpage");
		}
		return mv;
	}
	
	@PostMapping(value = "/adminregistration")
	public  ModelAndView createNewEmployeeUser(@Valid EmployeeModel employeeModel,	BindingResult bindingResult, 
			ModelAndView mv, @RequestParam(value="file") MultipartFile image) throws IOException {

		/**
		 * Converts the file to a byte format and then stores it unto the database
		 */
		employeeModel.setImage(image.getBytes());
		
		UserModel userExists = userService.findUserEmail(employeeModel.getEmail());
		
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.email",
							"There is already a user with the email provided");
		}
		if (!bindingResult.hasErrors()) {
			
			employeeModel.setStatus(1);
	        RoleModel empRole = roleService.roleExists("ADMIN");
	        employeeModel.setRoleName(new HashSet<RoleModel>(Arrays.asList(empRole)));
	        
			empService.saveEmployee(employeeModel);
			mv.addObject("employeeModel", new EmployeeModel());
			mv.addObject("successMessage", "Employee has been registered successfully");
			mv.setViewName("login");
		}else {
			bindingResult
			.rejectValue("email", "error.email", bindingResult.toString());
		}
		return mv;
	}
}
