package com.example.examapp.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
				+ "' alt='employeeImage' class='img-rounded studentProfileImage'/>");

		mv.addObject("employee", employee);
		mv.addObject("employeeModel", new EmployeeModel());
		mv.setViewName("employeeprofile");
		return mv;
	}

	@PostMapping(value = "/employeeprofile")
	public RedirectView updateEmployeeDetail(@Valid @ModelAttribute("employeeModel") EmployeeModel employeeModel,
			BindingResult bindingResult, ModelAndView mv) {

		EmployeeModel employeeUpdate = empService.findById(employeeModel.getUserId());

		employeeUpdate.setFirstName(employeeModel.getFirstName());
		employeeUpdate.setLastName(employeeModel.getLastName());
		employeeUpdate.setOtherName(employeeModel.getOtherName());
		employeeUpdate.setEmail(employeeModel.getEmail());
		if (!bindingResult.hasErrors()) {
			empService.updateEmployee(employeeUpdate);
			mv.addObject("successMessage", "Employee has been registered successfully");
		} else {
			bindingResult.rejectValue("email", "error.email", bindingResult.toString());
		}
		return new RedirectView("/employeeprofile/"+employeeModel.getUserId());
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
