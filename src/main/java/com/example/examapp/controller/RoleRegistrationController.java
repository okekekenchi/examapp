package com.example.examapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.model.RoleModel;
import com.example.examapp.service.RoleService;

@RestController
public class RoleRegistrationController {

	@Autowired private RoleService roleService;
	
	@GetMapping("/roleregistration")
	public ModelAndView getRoleRegistration(ModelAndView mv) {
		
		mv.addObject("roleModel", new RoleModel());
		mv.setViewName("roleregistration");
		return mv;
	}
	
	@PostMapping(value = "/roleregistration")
	public ModelAndView createNewUser(@Valid  @ModelAttribute("roleModel") RoleModel roleModel,
			BindingResult bindingResult, ModelAndView mv) {
				
		RoleModel roleExists = roleService.findByRoleName(roleModel.getRoleName());
		if (roleExists != null) {
			bindingResult
					.rejectValue("roleName", "error.roleName",
							"There is already a role registered similar to the one provided");
		}
		if (!bindingResult.hasErrors()) {
			roleService.saveRole(roleModel);
			mv.addObject("roleModel", new RoleModel());
		    mv.addObject("successMessage",
		    		"RoleModel has been registered successfully");	
		}
		return mv;
	}
}
