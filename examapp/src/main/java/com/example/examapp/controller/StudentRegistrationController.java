package com.example.examapp.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.model.RegistrationCardModel;
import com.example.examapp.model.StudentModel;
import com.example.examapp.model.UserModel;
import com.example.examapp.service.CardVerificationService;
import com.example.examapp.service.CourseService;
import com.example.examapp.service.RoleService;
import com.example.examapp.service.SettingsService;
import com.example.examapp.service.StudentService;
import com.example.examapp.service.UserService;

@RestController
public class StudentRegistrationController {
	
	@Autowired CourseService courseService;
	
	@Autowired StudentService studentService;

	@Autowired SettingsService settingsService;

	@Autowired UserService userService;

	@Autowired RoleService roleService;
	
	@Autowired private CardVerificationService cvService;
	
	@PostMapping(value = "/studentregistration")
	public  ModelAndView createNewStudentUser(@Valid @ModelAttribute("studentModel") StudentModel studentModel,
			BindingResult bindingResult, HttpServletRequest request, ModelAndView mv,
			@RequestParam(value="file") MultipartFile image) throws IOException {
		
		Enumeration<String> parameterNames = request.getParameterNames();
		long serialNumber = 0;
    	if(parameterNames.hasMoreElements()) {
    		serialNumber = Long.valueOf(request.getParameter("serialNumber"));
    	}

		/**
		 * Converts the file to a byte format and then stores it unto the database
		 */
		studentModel.setImage(image.getBytes());
		
		UserModel userExists = userService.findUserEmail(studentModel.getEmail());
		
		if (userExists != null) {
			studentModel.setEmail("");
			bindingResult
					.rejectValue("email", "error.email",
							"*There is already a user registered with the email provided");
		}
		if (!bindingResult.hasErrors()) {
			studentModel.setStatus(1);
			studentService.saveStudent(studentModel);
			
			RegistrationCardModel rcm = cvService.findBySerialNumber(serialNumber);
			rcm.setActive(0);
			cvService.save(rcm);
			
			mv.setViewName("registrationsuccess");
			mv.addObject("successMessage", "Registration successfully!");
		}else {
			mv.setViewName("cardverfication");
		}
		return mv;
	}
}
