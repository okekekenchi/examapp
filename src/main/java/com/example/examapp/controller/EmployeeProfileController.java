package com.example.examapp.controller;

import java.util.Enumeration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.datatablemodel.Employee;
import com.example.examapp.model.EmployeeModel;
import com.example.examapp.service.EmployeeService;
import com.google.gson.Gson;

@RestController
public class EmployeeProfileController {

	@Autowired
	private EmployeeService empService;

	@PersistenceContext
	private EntityManager entityManager;

	private Employee employee;
	private EmployeeModel employeeModel;

	@GetMapping(value = "/employeeprofile/{id}")
	public ModelAndView employeeProfile(ModelAndView mv, @PathVariable int id) {
		employeeModel = empService.findById(id);

		employee = new Employee(id, employeeModel.getFirstName(), employeeModel.getLastName(), employeeModel.getOtherName(),
				employeeModel.getEmail(),
				employeeModel.getStatus(), employeeModel.getOnline(), employeeModel.getRegdate(), ""
				);

		if (employee.getStatus() == 1) {
			employee.setValidStatus("<img src='/images/active.png' alt='Active' style='width:30px; height:30px;' />");
		} else {
			employee.setValidStatus("<img src='/images/inactive.png' alt='Inactive' style='width:30px; height:30px;' />");
		}
		
		if(employee.getOnline() == 1) {
			employee.setOnlineStatus("<img src='/images/Online.png' alt='Active' style='width:30px; height:30px;'/>");
		}else {
			employee.setOnlineStatus("<img src='/images/offline.png' alt='Inactive' style='width:30px; height:30px;' />");
		}
		employee.setProfileImage("<img src='/Image/" + id + "' name='image' id='" + id
				+ "' alt='employeeImage' class='img-rounded userProfileImage'/>");

		mv.addObject("employee", employee);
		mv.addObject("employeeModel", new EmployeeModel());
		mv.setViewName("employeeprofile");
		return mv;
	}
	
	@GetMapping(value = "/employeeprofile")
	public ModelAndView getEmployeeProfile(ModelAndView mv) {
		
		String Username = SecurityContextHolder.getContext().getAuthentication().getName();
		EmployeeModel employeeModel = empService.findEmployeeEmail(Username);
		return employeeProfile(mv, employeeModel.getUserId());
	}

	@PostMapping(value = "/employeeprofile")
	public String updateEmployeeDetail(HttpServletRequest request, EmployeeModel employeeUpdate) {
		
		Enumeration<String> parameterNames = request.getParameterNames();

    	if(parameterNames.hasMoreElements()) {
    		
    		employeeUpdate = empService.findById(Integer.parseInt(request.getParameter("userId")));
    		employeeUpdate.setFirstName(String.valueOf(request.getParameter("firstName")));
    		employeeUpdate.setLastName(String.valueOf(request.getParameter("lastName")));
    		employeeUpdate.setOtherName(String.valueOf(request.getParameter("otherName")));
    		employeeUpdate.setEmail(String.valueOf(request.getParameter("email")));
			empService.updateEmployee(employeeUpdate);
    		
    	}
		String msg = "Update Successful";
		return new Gson().toJson(msg);
	}

	@GetMapping("/employeeprofile/fetch/{id}")
	public String fetchEmployee(@PathVariable int id) {
		employeeModel = empService.findById(id);
		employee = new Employee(id, employeeModel.getFirstName(), employeeModel.getLastName(), employeeModel.getOtherName(),
				employeeModel.getEmail(), employeeModel.getStatus(),
				employeeModel.getOnline(), employeeModel.getRegdate(), ""
				);
		// Persist ID
		employee.setUserId(id);

		return new Gson().toJson(employee);
	}
}
