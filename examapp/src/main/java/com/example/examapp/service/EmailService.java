package com.example.examapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.examapp.model.QuestionProp;
import com.example.examapp.model.StudentModel;

@Component
public class EmailService {

	@Autowired
    private JavaMailSender javaMailSender;
	
	public void sendSimpleEmail(List<QuestionProp> questionProp, StudentModel studentModel) {
		String Message = "Hello "+ studentModel.getFirstName() + "\n\n";
		int totalScore = 0;
		for(QuestionProp qp : questionProp) {
			Message += qp.getSubjectName() + " : " + qp.getTotalScore() + "\n";
			totalScore += qp.getTotalScore();
		}
		
		Message += "\nTOTAL SCORE : " + totalScore;
		
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			
	        msg.setTo(studentModel.getEmail());
	        msg.setSubject("Test Scores");
	        msg.setText(Message);

	        javaMailSender.send(msg);
	        
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void sendSimpleEmail(String email, String pin, String serialNumber) {
		String Message = "Kenmaxi University of Science and Technology\n\n";
				
		Message += "Serial Number : " + serialNumber + ".\n";
		Message += "Pin Number : " + pin + ".\n\n\n\n\n";
		Message += "Kindly visit 127.0.0.1:9090 to register.";
		
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			
	        msg.setTo(email);
	        msg.setSubject("Registration Voucher");
	        msg.setText(Message);

	        javaMailSender.send(msg);
	        
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}