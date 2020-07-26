package com.example.examapp.controller;

import java.util.Enumeration;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.model.RegistrationCardModel;
import com.example.examapp.service.CardVerificationService;
import com.example.examapp.service.EmailService;

@RestController
public class PinGeneratorController {

	@Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired private CardVerificationService cvService;

	@Autowired private EmailService emailService;

	@GetMapping("/pingenerator")
	public ModelAndView getPinGenerator(ModelAndView mv) {
		
		mv.setViewName("pingenerator");
		return mv;
	}
	
	@PostMapping("/pingenerator")
	public ModelAndView generatePin (ModelAndView mv, HttpServletRequest request) {
		Enumeration<String> parameterNames = request.getParameterNames();
		RegistrationCardModel rcm = new RegistrationCardModel();
		String email = String.valueOf(request.getParameter("email"));
		int SN = ThreadLocalRandom.current().nextInt();
		
		if(SN < 0)
			rcm.setSerialNumber(SN * (-1));
		else
			rcm.setSerialNumber(SN);
				
		String pin = randomAlphaNumeric(true);
		System.out.println(pin);
		rcm.setPinNumber(bCryptPasswordEncoder.encode(pin));
		rcm.setEmail(email);
		rcm.setActive(1);
		cvService.save(rcm);
		
		if(parameterNames.hasMoreElements())
			emailService.sendSimpleEmail(email, pin, String.valueOf(rcm.getSerialNumber()));
		
		mv.setViewName("pingenerator");
		return mv;
	}
	
	private String randomAlphaNumeric( boolean numberfirst) {
		char[] character = {'1','2','3','4','5','6','7','8','9','0','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'}; 
		int randomCharacter, counter = 0;
		int numberOfCharacters = 4;
		String returnString = "";
		while(numberOfCharacters != 0) {
			if(numberfirst) {
				randomCharacter = ThreadLocalRandom.current().nextInt( 0, 10);
				returnString += character[randomCharacter];
				numberfirst = false;
			}else {
				randomCharacter = ThreadLocalRandom.current().nextInt( 10, character.length);
				returnString += character[randomCharacter];
				numberfirst = true;
			}
			numberOfCharacters--;
			if(numberOfCharacters == 0 && counter < 4) {
				returnString += "-";
				counter++;
				numberOfCharacters = 4;
			}			
		}
		return returnString;
	}
}
