package com.example.examapp.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.examapp.model.StudentModel;
import com.example.examapp.model.SubjectModel;
import com.example.examapp.service.StudentService;
import com.example.examapp.service.SubjectService;

public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	StudentService studentService;
	@Autowired
	SubjectService subjectService;
	protected final Log logger = LogFactory.getLog(this.getClass());
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	public MySimpleUrlAuthenticationSuccessHandler() {
		super();
	}

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication auth) throws IOException, ServletException {

		handle(request, response, auth);
		clearAuthenticationAttributes(request);
	}

	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException {
		String targetUrl = determineTargetUrl(auth);

		if (response.isCommitted()) {
			logger.debug("Response has already been commited. unable to redirect to " + targetUrl);
			return;
		}

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(Authentication auth) {
		boolean isUser = false;
		boolean isAdmin = false;
		boolean isStudent = false;
		boolean isVendor = false;
		int subjectId = 0;
		List<SubjectModel> subjectList = new ArrayList<>();

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
				isUser = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_STUDENT")) {
				isStudent = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_VENDOR")) {
				isVendor = true;
				break;
			}
		}

		if (isUser) {
			return "/student";
		} else if (isAdmin) {
			return "/";
		} else if (isStudent) {

			String Username = SecurityContextHolder.getContext().getAuthentication().getName();
			StudentModel studentModel = studentService.findStudentEmail(Username);

			if(studentModel != null) {
				if(studentModel.getStatus() == 0) {
					return "/logout"; 
				}
			}

			try {
				subjectList = subjectService.subjectRegistered(studentModel.getCourseModel().getCourseId());

				if (subjectList.size() > 0)
					subjectId = subjectList.get(0).getSubjectId();

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}

			return "/examquestion/" + subjectId + "/6565";
		} else if (isVendor) {
			return "/";
		} else {
			return "/";
		}
	}

	// Clears any existing session before redirecting
	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}