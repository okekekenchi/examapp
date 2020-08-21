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

import com.example.examapp.datatablemodel.Vendor;
import com.example.examapp.model.VendorModel;
import com.example.examapp.service.VendorService;
import com.google.gson.Gson;

@RestController
public class VendorProfileController {

	@Autowired
	private VendorService vendorService;

	@PersistenceContext
	private EntityManager entityManager;

	private Vendor vendor;
	private VendorModel vendorModel;

	@GetMapping(value = "/vendorprofile/{id}")
	public ModelAndView vendorProfile(ModelAndView mv, @PathVariable int id) {
		vendorModel = vendorService.findById(id);

		vendor = new Vendor(id, vendorModel.getFirstName(), vendorModel.getLastName(), vendorModel.getOtherName(),
				vendorModel.getEmail(),
				vendorModel.getStatus(), vendorModel.getOnline(), vendorModel.getRegdate(), ""
				);

		if (vendor.getStatus() == 1) {
			vendor.setValidStatus("<img src='/images/active.png' alt='Active' style='width:30px; height:30px;' />");
		} else {
			vendor.setValidStatus("<img src='/images/inactive.png' alt='Inactive' style='width:30px; height:30px;' />");
		}
		
		if(vendor.getOnline() == 1) {
			vendor.setOnlineStatus("<img src='/images/Online.png' alt='Active' style='width:30px; height:30px;'/>");
		}else {
			vendor.setOnlineStatus("<img src='/images/offline.png' alt='Inactive' style='width:30px; height:30px;' />");
		}
		vendor.setProfileImage("<img src='/Image/" + id + "' name='image' id='" + id
				+ "' alt='vendorImage' class='img-rounded userProfileImage'/>");

		mv.addObject("vendor", vendor);
		mv.addObject("vendorModel", new VendorModel());
		mv.setViewName("vendorprofile");
		return mv;
	}
	
	@GetMapping(value = "/vendorprofile")
	public ModelAndView getVendorProfile(ModelAndView mv) {
		
		String Username = SecurityContextHolder.getContext().getAuthentication().getName();
		VendorModel vendorModel = vendorService.findVendorByEmail(Username);
		return vendorProfile(mv, vendorModel.getUserId());
	}

	@PostMapping(value = "/vendorprofile")
	public String updateVendorDetail(HttpServletRequest request, VendorModel vendorUpdate) {
		
		Enumeration<String> parameterNames = request.getParameterNames();

    	if(parameterNames.hasMoreElements()) {
    		
    		vendorUpdate = vendorService.findById(Integer.parseInt(request.getParameter("userId")));
    		vendorUpdate.setFirstName(String.valueOf(request.getParameter("firstName")));
    		vendorUpdate.setLastName(String.valueOf(request.getParameter("lastName")));
    		vendorUpdate.setOtherName(String.valueOf(request.getParameter("otherName")));
    		vendorUpdate.setEmail(String.valueOf(request.getParameter("email")));
			vendorService.updateVendor(vendorUpdate);
    		
    	}
		String msg = "Update Successful";
		return new Gson().toJson(msg);
	}

	@GetMapping("/vendorprofile/fetch/{id}")
	public String fetchVendor(@PathVariable int id) {
		vendorModel = vendorService.findById(id);
		vendor = new Vendor(id, vendorModel.getFirstName(), vendorModel.getLastName(), vendorModel.getOtherName(),
				vendorModel.getEmail(), vendorModel.getStatus(),
				vendorModel.getOnline(), vendorModel.getRegdate(), ""
				);
		// Persist ID
		vendor.setUserId(id);

		return new Gson().toJson(vendor);
	}
}
