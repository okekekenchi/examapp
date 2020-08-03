package com.example.examapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.model.DashboardModel;
import com.example.examapp.model.UserModel;
import com.example.examapp.service.CourseService;
import com.example.examapp.service.EmployeeService;
import com.example.examapp.service.QuestionService;
import com.example.examapp.service.RoleService;
import com.example.examapp.service.SettingsService;
import com.example.examapp.service.StudentService;
import com.example.examapp.service.SubjectService;
import com.example.examapp.service.UserService;
import com.example.examapp.service.VoucherService;

@RestController
public class HomeController {
	
	@Autowired StudentService studentService;
	
	@Autowired SubjectService subjectService;
	
	@Autowired EmployeeService employeeService;

	@Autowired UserService userService;
	
	@Autowired RoleService roleService;
	
	@Autowired CourseService courseService;
	
	@Autowired QuestionService questionService;
	
	@Autowired SettingsService settingsService;

	@Autowired VoucherService voucherService;

	@GetMapping(value="/")
	public ModelAndView getDashboard(ModelAndView mv, DashboardModel dashboard, Authentication authentication){		
		
		UserModel userModel = userService.findUserEmail(authentication.getName());		
		String image = "<img src='/Image/"+userModel.getUserId()+"' alt='userImage' class='img-rounded' style='width:40px; height:40px; border-radius: 30px;'/>";


		dashboard.setNstudent(studentService.getActiveStudents());
		dashboard.setUsername(userModel.getFirstName());
		dashboard.setProfile(image);
		dashboard.setNemployee(employeeService.getActiveEmployees());
		dashboard.setNstudentOnline(studentService.getStudentsOnline());
		dashboard.setNemployeeOnline(employeeService.getEmployeesOnline());
		dashboard.setNcourse(courseService.nActiveCourses());
		dashboard.setNcomputer(settingsService.getSetting().getNComputers());
		dashboard.setNvendor(employeeService.getActiveVendors());
		dashboard.setNvendorOnline(employeeService.getVendorsOnline());
		dashboard.setNvoucher(voucherService.getnVouchers());
		dashboard.setNvoucherUsed(voucherService.getnVouchersUsed());
		dashboard.setNsubject(subjectService.activeSubjects());
		dashboard.setNquestion(questionService.nQuestions());
		dashboard.setNrole(roleService.nRoles());
		
		mv.addObject("dashboard", dashboard);
		mv.setViewName("index");
		return mv;
	}
}
