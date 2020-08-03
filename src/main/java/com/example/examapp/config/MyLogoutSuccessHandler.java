package com.example.examapp.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.examapp.model.UserModel;
import com.example.examapp.service.UserService;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
	
	@Autowired UserService userService;

	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		if(authentication != null) {
			
			UserModel userModel = userService.findUserEmail(authentication.getName());
			userModel.setOnline(0);
			userService.saveUser(userModel);
		}
		
		String URL = request.getContextPath() + "/login";
		response.setStatus(HttpStatus.OK.value());
		response.sendRedirect(URL);
	}
}
