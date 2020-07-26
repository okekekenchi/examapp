package com.example.examapp.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.datatablemodel.Role;
import com.example.examapp.model.RoleModel;
import com.example.examapp.pagination.DataTableResults;
import com.example.examapp.service.RoleService;
import com.google.gson.Gson;

@RestController
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	//@RolesAllowed("ADMIN")
	@GetMapping(value="/role")
	public ModelAndView getRegistrationPage(ModelAndView mv){
		mv.addObject("roleModel", new RoleModel());
		return mv;
	}
	
	//@RolesAllowed({"ADMIN"})
	@DeleteMapping("/role")
	public void deleteRole(HttpServletRequest request, HttpServletResponse response) {
		
		Enumeration<String> parameterNames = request.getParameterNames();
    	
    	if(parameterNames.hasMoreElements()) {
			roleService.deleteCourse(Integer.parseInt(request.getParameter("roleId")));
    	}
	}
	
	@PostMapping("/role/fetch")
	@ResponseBody
	public String fetchRole(HttpServletRequest request, HttpServletResponse response, RoleModel roleModel) {
		Enumeration<String> parameterNames = request.getParameterNames();
    	
    	if(parameterNames.hasMoreElements()) {    			
    		roleModel = roleService.findById(Integer.parseInt(request.getParameter("roleId")));
		}
    	return new Gson().toJson(roleModel);
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/role/paginated/")
	public String listRolesPaginated(HttpServletRequest request, HttpServletResponse response) {
		
		List<RoleModel> roleModelList = new ArrayList<>();
		List<Role> roleList = new ArrayList<>();
		DataTableResults<Role> dataTableResult = new DataTableResults<Role>();
		Enumeration<String> parameterNames = request.getParameterNames();	
		
    	if(parameterNames.hasMoreElements()) {    		
    		Query query = entityManager.createNativeQuery(
    				roleService.dataTableQuery(request), RoleModel.class);
    		roleModelList = query.getResultList();

    		Query recordCount = entityManager.createNativeQuery("SELECT * FROM RoleModel");
  
    		for(int i=0; i<roleModelList.size() ; i++) {
    			roleList.add(
					new Role(
						roleModelList.get(i).getRoleId(),
						roleModelList.get(i).getRoleName()
					)
				);
    		}
    		
    		for(Role role : roleList) {

    			role.setUpdate("<img src='/images/edit.png' name='update' class='update' id='"+role.getRoleId()+"' alt='Update'/>");
    			role.setDelete("<img src='/images/delete.png'  style='width:25px; height:25px;' name='delete'  class='delete' id='"+role.getRoleId()+"' alt='Delete'/>");
      		}
    		
    		dataTableResult.setDraw(request.getParameter("draw").toString());
    		dataTableResult.setRecordsFiltered(Integer.toString(recordCount.getResultList().size()));
    		dataTableResult.setRecordsTotal(Integer.toString(roleList.size()));
    		dataTableResult.setListOfDataObjects(roleList);		
		}
		return new Gson().toJson(dataTableResult);
	}
}
