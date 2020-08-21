package com.example.examapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.datatablemodel.Vendor;
import com.example.examapp.model.VendorModel;
import com.example.examapp.model.RoleModel;
import com.example.examapp.model.UserModel;
import com.example.examapp.pagination.DataTableResults;
import com.example.examapp.service.VendorService;
import com.example.examapp.service.RoleService;
import com.example.examapp.service.UserService;
import com.google.gson.Gson;

@RestController
public class VendorController {
	
	@Autowired private VendorService vendorService;
	
	@Autowired private UserService userService;
	
	@Autowired private RoleService roleService;

	
	@PersistenceContext
	private EntityManager entityManager;
	
	@GetMapping(value="/vendor")
	public ModelAndView registration(ModelAndView mv){
		
		mv.addObject("vendorModel", new VendorModel());
		mv.setViewName("/vendor");
		return mv;
	}
	
	@PostMapping(value = "/vendor")
	public  ModelAndView createNewVendorUser(@Valid VendorModel vendorModel, BindingResult bindingResult,
												ModelAndView mv, HttpServletRequest request,
												@RequestParam(value="file") MultipartFile image) throws IOException {
				
		/**
		 * Converts the file to a byte format and then stores it unto the database
		 */
    	
		vendorModel.setImage(image.getBytes());
		
		UserModel userExists = userService.findUserEmail(vendorModel.getEmail());
		
		if (userExists != null) {
			bindingResult
				.rejectValue("email", "error.email",
					"There is already a vendor with the email provided");
		}
		
		if (!bindingResult.hasErrors()) {
			
			vendorModel.setStatus(1);
			RoleModel role;
			role = roleService.roleExists("VENDOR");

			vendorModel.setRoleName(new HashSet<RoleModel>(Arrays.asList(role)));
	        
			vendorService.saveVendor(vendorModel);
			mv.addObject("vendorModel", new VendorModel());
			mv.addObject("successMessage", "Vendor has been registered successfully");		
		}else {
			bindingResult
			.rejectValue("email", "error.email", bindingResult.toString());
		}
		return mv;
	}

	@DeleteMapping("/vendor")
	public void deleteVendor(HttpServletRequest request) {
		VendorModel vendorModel = new VendorModel();
		Enumeration<String> parameterNames = request.getParameterNames();

    	if(parameterNames.hasMoreElements()) {
    		if(Integer.parseInt(request.getParameter("vendorStatus")) == 1) {
    			vendorModel = vendorService.findById(Integer.parseInt(request.getParameter("vendorId")));
    			vendorModel.setStatus(0);
    			vendorService.updateVendor(vendorModel);
    		}
    	}
	}
		
	@PostMapping("vendor/logout")
	public void logoutVendor(HttpServletRequest request) {
		VendorModel vendorModel = new VendorModel();
		Enumeration<String> parameterNames = request.getParameterNames();

    	if(parameterNames.hasMoreElements()) {
			vendorModel = vendorService.findById(Integer.parseInt(request.getParameter("vendorId")));
			vendorModel.setOnline(0);
			vendorService.updateVendor(vendorModel);
    	}
    	System.out.println(request.getParameter("vendorId"));
	}
	
	@PostMapping("/vendor/view")
	@ResponseBody
	public String fetchVendorDetail(HttpServletRequest request, HttpServletResponse response, VendorModel vendorModel) {
		Enumeration<String> parameterNames = request.getParameterNames();
    	String vendorDetail = "";
    	if(parameterNames.hasMoreElements()) {
			vendorModel = vendorService.findById(Integer.parseInt(request.getParameter("vendorId")));
			
			vendorDetail += " <div class='table-responsive'> "+ 
							"<table class='table table-boredered'> "+
								"<tr> "+
										"<td>First Name</td> "+
										"<td>"+vendorModel.getFirstName()+"</td> "+
								"</tr> "+
								"<tr> "+
									"<td>Last Name</td> "+
									"<td>"+vendorModel.getLastName()+"</td> "+
								"</tr> ";
									if(!vendorModel.getOtherName().isEmpty()) {
					vendorDetail +=	"<tr> "+
											"<td>Other Name</td> "+
											"<td>"+vendorModel.getOtherName()+"</td> "+
										"</tr> ";
									}
				vendorDetail +="<tr> "+
									"<td>Email</td> "+
									"<td>"+vendorModel.getEmail()+"</td> "+
								"</tr> "+
								"<tr> "+
									"<td>Registration Date</td> "+
									"<td>"+vendorModel.getRegdate()+"</td> "+
								"</tr> ";
			vendorDetail +=   "<tr> "+
							"</table>" + 
						"</div>";		
		}
    	return vendorDetail;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/vendor/paginated/")
	@ResponseBody
	public String listVendorPaginated(HttpServletRequest request, HttpServletResponse response)
						throws ServletException, IOException{
		
		List<VendorModel> vendorModelList = new ArrayList<>();
		List<Vendor> vendorList = new ArrayList<>();
		DataTableResults<Vendor> dataTableResult = new DataTableResults<Vendor>();
		Enumeration<String> parameterNames = request.getParameterNames();
		
    	if(parameterNames.hasMoreElements()) {
    		Query query = entityManager.createNativeQuery(
    				vendorService.dataTableQuery(request), VendorModel.class);
    		vendorModelList = query.getResultList();

    		Query recordCount = entityManager.createNativeQuery("SELECT * FROM vendorModel");
    		    		
    		for(int i=0; i<vendorModelList.size() ; i++) {
    			vendorList.add(
					new Vendor(
							vendorModelList.get(i).getUserId(),
							vendorModelList.get(i).getFirstName(),
							vendorModelList.get(i).getLastName(),
							vendorModelList.get(i).getOtherName(),
							vendorModelList.get(i).getEmail(),
							vendorModelList.get(i).getStatus(),
							vendorModelList.get(i).getOnline(),
							""
						)
					);
    		}
    		
    		for(Vendor emp : vendorList) {
    			if(emp.getStatus() == 1) {
    				emp.setValidStatus("<img src='/images/active.png' alt='Active' style='width:25px; height:25px;'/>");
        			emp.setDelete("<img src='/images/delete.png'  style='width:25px; height:25px;' name='delete' class='delete' id='"+emp.getUserId()+"' data-status='"+emp.getStatus()+"' alt='Delete'>");
    			}else {
    				emp.setValidStatus("<img src='/images/inactive.png' alt='Inactive' style='width:25px; height:25px;'/>");
        			emp.setDelete("<img src='/images/delete.png' onclick='return;' style='width:25px; height:25px;' name='delete' class='delete' id='"+emp.getUserId()+"' data-status='"+emp.getStatus()+"' alt='Delete'>");
    			}
    			
    			if(emp.getOnline() == 1) {
    				emp.setOnlineStatus("<img src='/images/Online.png' alt='Active' style='width:25px; height:25px;'/>");
        			emp.setLogout("<img src='/images/logout1.png'  style='width:25px; height:25px;' name='logout' class='logout' id='"+emp.getUserId()+"' alt='Logout'>");
    			}else {
    				emp.setOnlineStatus("<img src='/images/offline.png' alt='Inactive' style='width:25px; height:25px;' />");
        			emp.setLogout("<img src='/images/logout1.png' onclick='return;' style='width:25px; height:25px;' name='logout' class='logout' id='"+emp.getUserId()+"' alt='Logout'>");
    			}
			    			
    			emp.setView("<img src='/images/view.png' name='view' id='"+emp.getUserId()+"' class='view' alt='View' style='width:25px; height:25px;'>");
    								
    			emp.setProfileImage("<a href='/vendorprofile/"+emp.getUserId()+"' ><img src='/images/edit.png' name='image' id='"+emp.getUserId()+"' alt='vendorImage' style='zoom:1'/></a>");
    		}
    		
    		dataTableResult.setDraw(request.getParameter("draw").toString());
    		dataTableResult.setRecordsFiltered(Integer.toString(recordCount.getResultList().size()));
    		dataTableResult.setRecordsTotal(Integer.toString(vendorList.size()));
    		dataTableResult.setListOfDataObjects(vendorList);		
		}
		return new Gson().toJson(dataTableResult);
	}
}
