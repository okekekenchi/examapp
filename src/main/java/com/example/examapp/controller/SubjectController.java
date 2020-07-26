package com.example.examapp.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.datatablemodel.Subject;
import com.example.examapp.model.SubjectModel;
import com.example.examapp.pagination.DataTableResults;
import com.example.examapp.service.SubjectService;
import com.google.gson.Gson;

@RestController
public class SubjectController {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private SubjectService subjectService;

	@GetMapping(value = "/subject")
	public ModelAndView getSubject(ModelAndView mv) {
		mv.addObject("subjectModel", new SubjectModel());
		return mv;
	}

	//This function adds and makes updates
	@PostMapping("/subject")
	public ModelAndView addSubject(@Valid SubjectModel subjectModel, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView();
		SubjectModel subjectExists = subjectService.findBySubject(subjectModel.getSubjectName());
		if (subjectExists != null) {
			bindingResult.rejectValue("subjectName", "error.subjectName",
					"There is already a registered subject similar to the one provided");
			mv.addObject("Message", "There is already a registered subject similar to the one provided");
		}
		if (!bindingResult.hasErrors()) {
			
			subjectModel.setSubjectStatus(1);
			subjectService.saveSubject(subjectModel);
			mv.addObject("subjectModel", new SubjectModel());
			mv.addObject("Message", "Subject has been added successfully");
		}
		return mv;
	}
		
	@DeleteMapping("/subjects")
	public String deleteSubject(HttpServletRequest request, HttpServletResponse response, SubjectModel subjectModel) {
		
		Enumeration<String> parameterNames = request.getParameterNames();
		
    	if(parameterNames.hasMoreElements()) {
    		if(Integer.parseInt(request.getParameter("subjectStatus")) == 1) {
    			subjectModel = subjectService.findById(Integer.parseInt(request.getParameter("subjectId")));
    			subjectModel.setSubjectStatus(0);
    			subjectService.saveSubject(subjectModel);
    		}
    	}
    	return new Gson().toJson(subjectModel);
	}
	
	@PostMapping("/subjects/fetch")
	@ResponseBody
	public String fetchSubject(HttpServletRequest request, HttpServletResponse response, SubjectModel subjectModel) {
		Enumeration<String> parameterNames = request.getParameterNames();
		
    	if(parameterNames.hasMoreElements()) {
    		subjectModel = subjectService.findById(Integer.parseInt(request.getParameter("subjectId")));
		}
    	
    	Subject subject = new Subject(subjectModel.getSubjectId(), subjectModel.getSubjectName(),
    									subjectModel.getSubjectStatus());
    	return new Gson().toJson(subject);
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/subjects/paginated/")
	public String listSubjectsPaginated(HttpServletRequest request, HttpServletResponse response) {
		
		List<SubjectModel> subjectModelList = new ArrayList<>();
		List<Subject> subjectList = new ArrayList<>();
		DataTableResults<Subject> dataTableResult = new DataTableResults<Subject>();		
		Enumeration<String> parameterNames = request.getParameterNames();	
		
    	if(parameterNames.hasMoreElements()) {    		
    		Query query = entityManager.createNativeQuery(
    				subjectService.dataTableQuery(request), SubjectModel.class);
    		subjectModelList = query.getResultList();

    		Query recordCount = entityManager.createNativeQuery("SELECT * FROM SubjectModel");
  
    		for(int i=0; i<subjectModelList.size() ; i++) {
    			subjectList.add(
    					new Subject(
    							subjectModelList.get(i).getSubjectId(),
    							subjectModelList.get(i).getSubjectName(),
    							subjectModelList.get(i).getSubjectStatus()
    							)
    					);
    		}
    		
    		for(Subject subject : subjectList) {
    			
    			if(subject.getSubjectStatus() == 1) {
    				subject.setValidStatus("<img src='/images/active.png' alt='Active' style='width:25px; height:25px;'/>");
    			}else {
    				subject.setValidStatus("<img src='/images/inactive.png' alt='Inactive' style='width:25px; height:25px;'/>");
        		}  
    			
    			subject.setUpdate("<img src='/images/edit.png'  style='width:25px; height:25px;' name='update' class='update' id='"+subject.getSubjectId()+"' data-status='"+subject.getSubjectStatus()+"' alt='Delete'>");
    			subject.setDelete("<img src='/images/delete.png' style='width:25px; height:25px;' name='delete' class='delete' id='"+subject.getSubjectId()+"' data-status='"+subject.getSubjectStatus()+"' alt='Delete'>");
    		
    		}
    		
    		
    		dataTableResult.setDraw(request.getParameter("draw").toString());
    		dataTableResult.setRecordsFiltered(Integer.toString(recordCount.getResultList().size()));
    		dataTableResult.setRecordsTotal(Integer.toString(subjectList.size()));
    		dataTableResult.setListOfDataObjects(subjectList);		
		}
		return new Gson().toJson(dataTableResult);
	}
}