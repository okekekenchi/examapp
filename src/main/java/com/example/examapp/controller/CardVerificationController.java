package com.example.examapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.model.RegistrationCardModel;
import com.example.examapp.model.StudentModel;
import com.example.examapp.service.CardVerificationService;
import com.example.examapp.service.CourseService;

@RestController
public class CardVerificationController {

	@Autowired private CourseService courseService;
	
	@Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired private CardVerificationService cvService;
	
	@GetMapping("/cardverification")
	public ModelAndView getCardVerification( ModelAndView mv) {
		
		mv.addObject("registrationCardModel", new RegistrationCardModel());
		mv.setViewName("cardverification");
		return mv;
	}
	
	@PostMapping("/cardverification")
	public ModelAndView printCardVerification( ModelAndView mv, RegistrationCardModel rcm, BindingResult bindingResult) {
		
		RegistrationCardModel rcmSerialExists;
		rcmSerialExists = cvService.findBySerialNumber(rcm.getSerialNumber());
		
		if (rcmSerialExists == null) {
			bindingResult
					.rejectValue("serialNumber", "error.serialNumber", "Invalid serial number try again");
		}else {
			if(rcmSerialExists.getActive() == 0) {
				bindingResult
				.rejectValue("serialNumber", "error.serialNumber", "Voucher has already been used");
			}
			
			if(!bCryptPasswordEncoder.matches(rcm.getPinNumber().toString(), rcmSerialExists.getPinNumber())) {				
				bindingResult
					.rejectValue("pinNumber", "error.pinNumber", "Invalid pin number try again");
			}else {
				if (!bindingResult.hasErrors()) {
					mv.addObject("serialNumber", rcmSerialExists.getSerialNumber());
					mv.addObject("courseList", courseService.getCourses());
					mv.addObject("studentModel", new StudentModel());
					mv.setViewName("studentregistration");
				}else {
					mv.addObject("serialNumber", rcmSerialExists.getSerialNumber());
					mv.addObject("registrationCardModel", new RegistrationCardModel());
					mv.setViewName("cardverification");
				}
			}
		}
		return mv;
	}
}
