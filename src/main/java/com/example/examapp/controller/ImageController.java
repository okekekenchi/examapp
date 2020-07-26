package com.example.examapp.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.examapp.model.StudentModel;
import com.example.examapp.model.UserModel;
import com.example.examapp.service.UserService;

@RestController
public class ImageController {
	
	@Autowired UserService userService;
	
	@GetMapping("/Image/{id}")
	public void getImage(HttpServletRequest request, HttpServletResponse response,  @PathVariable int id)
			throws ServletException, IOException{

			UserModel user = new StudentModel();
			user = userService.findById(id);
			response.setContentType("image/jpeg");
			ServletOutputStream stream = response.getOutputStream();
			stream.write(user.getImage());
			stream.close();
	}

}
