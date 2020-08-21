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

import com.example.examapp.datatablemodel.Employee;
import com.example.examapp.model.EmployeeModel;
import com.example.examapp.model.RoleModel;
import com.example.examapp.model.UserModel;
import com.example.examapp.pagination.DataTableResults;
import com.example.examapp.service.EmployeeService;
import com.example.examapp.service.RoleService;
import com.example.examapp.service.UserService;
import com.google.gson.Gson;

@RestController
public class EmployeeController {
	
	@Autowired private EmployeeService empService;
	
	@Autowired private UserService userService;
	
	@Autowired private RoleService roleService;

	
	@PersistenceContext
	private EntityManager entityManager;
	
	@GetMapping(value="/employee")
	public ModelAndView registration(ModelAndView mv){
		mv.addObject("employeeModel", new EmployeeModel());
		mv.setViewName("/employee");
		return mv;
	}
	
	@PostMapping(value = "/employee")
	public  ModelAndView createNewEmployeeUser(@Valid EmployeeModel employeeModel, BindingResult bindingResult,
												ModelAndView mv, HttpServletRequest request,
												@RequestParam(value="file") MultipartFile image) throws IOException {
		
		/**
		 * Converts the file to a byte format and then stores it unto the database
		 */
    	
		employeeModel.setImage(image.getBytes());
		
		UserModel userExists = userService.findUserEmail(employeeModel.getEmail());
		
		if (userExists != null) {
			bindingResult
				.rejectValue("email", "error.email",
					"There is already a user with the email provided");
		}
		
		if (!bindingResult.hasErrors()) {
			employeeModel.setStatus(1);
			RoleModel empRole;
			
			empRole = roleService.roleExists("USER");

			employeeModel.setRoleName(new HashSet<RoleModel>(Arrays.asList(empRole)));
	        
			empService.saveEmployee(employeeModel);
			mv.addObject("employeeModel", new EmployeeModel());
			mv.addObject("successMessage", "Employee has been registered successfully");		
		}else {
			bindingResult
			.rejectValue("email", "error.email", bindingResult.toString());
		}
		return mv;
	}

	@DeleteMapping("/employee")
	public void deleteEmployee(HttpServletRequest request) {
		EmployeeModel employeeModel = new EmployeeModel();
		Enumeration<String> parameterNames = request.getParameterNames();

    	if(parameterNames.hasMoreElements()) {
    		if(Integer.parseInt(request.getParameter("employeeStatus")) == 1) {
    			employeeModel = empService.findById(Integer.parseInt(request.getParameter("employeeId")));
    			employeeModel.setStatus(0);
    			empService.updateEmployee(employeeModel);
    		}
    	}
	}
		
	@PostMapping("employee/logout")
	public void logoutEmployee(HttpServletRequest request) {
		EmployeeModel employeeModel = new EmployeeModel();
		Enumeration<String> parameterNames = request.getParameterNames();

    	if(parameterNames.hasMoreElements()) {
			employeeModel = empService.findById(Integer.parseInt(request.getParameter("employeeId")));
			employeeModel.setOnline(0);
			empService.updateEmployee(employeeModel);
    	}
    	System.out.println(request.getParameter("employeeId"));
	}
	
	@PostMapping("/employee/view")
	@ResponseBody
	public String fetchEmployeeDetail(HttpServletRequest request, HttpServletResponse response, EmployeeModel employeeModel) {
		Enumeration<String> parameterNames = request.getParameterNames();
    	String employeeDetail = "";
    	if(parameterNames.hasMoreElements()) {
			employeeModel = empService.findById(Integer.parseInt(request.getParameter("employeeId")));
			
			employeeDetail += " <div class='table-responsive'> "+ 
							"<table class='table table-boredered'> "+
								"<tr> "+
										"<td>First Name</td> "+
										"<td>"+employeeModel.getFirstName()+"</td> "+
								"</tr> "+
								"<tr> "+
									"<td>Last Name</td> "+
									"<td>"+employeeModel.getLastName()+"</td> "+
								"</tr> ";
									if(!employeeModel.getOtherName().isEmpty()) {
					employeeDetail +=	"<tr> "+
											"<td>Other Name</td> "+
											"<td>"+employeeModel.getOtherName()+"</td> "+
										"</tr> ";
									}
				employeeDetail +="<tr> "+
									"<td>Email</td> "+
									"<td>"+employeeModel.getEmail()+"</td> "+
								"</tr> "+
								"<tr> "+
									"<td>Registration Date</td> "+
									"<td>"+employeeModel.getRegdate()+"</td> "+
								"</tr> ";
			employeeDetail +=   "<tr> "+
							"</table>" + 
						"</div>";		
		}
    	return employeeDetail;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/employee/paginated/")
	@ResponseBody
	public String listEmployeesPaginated(HttpServletRequest request, HttpServletResponse response)
						throws ServletException, IOException{
		
		List<EmployeeModel> employeeModelList = new ArrayList<>();
		List<Employee> employeeList = new ArrayList<>();
		DataTableResults<Employee> dataTableResult = new DataTableResults<Employee>();
		Enumeration<String> parameterNames = request.getParameterNames();
		
    	if(parameterNames.hasMoreElements()) {
    		Query query = entityManager.createNativeQuery(
    				empService.dataTableQuery(request), EmployeeModel.class);
    		employeeModelList = query.getResultList();

    		Query recordCount = entityManager.createNativeQuery("SELECT * FROM EmployeeModel");
    		    		
    		for(int i=0; i<employeeModelList.size() ; i++) {
    			employeeList.add(
					new Employee(
							employeeModelList.get(i).getUserId(),
							employeeModelList.get(i).getFirstName(),
							employeeModelList.get(i).getLastName(),
							employeeModelList.get(i).getOtherName(),
							employeeModelList.get(i).getEmail(),
							employeeModelList.get(i).getStatus(),
							employeeModelList.get(i).getOnline(),
							""
							)
					);
    		}
    		
    		for(Employee emp : employeeList) {
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
    								
    			emp.setProfileImage("<a href='/employeeprofile/"+emp.getUserId()+"' ><img src='/images/edit.png' name='image' id='"+emp.getUserId()+"' alt='employeeImage' style='zoom:1'/></a>");
    		}
    		
    		dataTableResult.setDraw(request.getParameter("draw").toString());
    		dataTableResult.setRecordsFiltered(Integer.toString(recordCount.getResultList().size()));
    		dataTableResult.setRecordsTotal(Integer.toString(employeeList.size()));
    		dataTableResult.setListOfDataObjects(employeeList);		
		}
		return new Gson().toJson(dataTableResult);
	}
}
