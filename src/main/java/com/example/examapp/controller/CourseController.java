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

import com.example.examapp.datatablemodel.Course;
import com.example.examapp.model.CourseModel;
import com.example.examapp.pagination.DataTableResults;
import com.example.examapp.service.CourseService;
import com.example.examapp.service.SubjectService;
import com.google.gson.Gson;

@RestController
public class CourseController {

	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private SubjectService subjectService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@GetMapping(value="/course")
	public ModelAndView getCourse(ModelAndView mv){
		mv.addObject("subjectList", subjectService.getSubjects());
		mv.addObject("courseModel", new CourseModel());
		return mv;
	}
	
	@PostMapping(value = "/course")
	public ModelAndView addCourse(@Valid CourseModel course, BindingResult bindingResult) {

		ModelAndView mv = new ModelAndView();
		CourseModel courseExists = courseService.findByCourse(course.getCourseName());
		
		if (courseExists == null){			
			if(course.getSubjectModels().size() > 0) {
				course.setCourseStatus(1);
				courseService.saveCourse(course);	
			    mv.addObject("successMessage", "Course has been registered successfully");
			}else {
				mv.addObject("successMessage", "Subjects must be associated with every course");
			}
		}else {
			if(courseExists.getRequiredApplicant() != course.getRequiredApplicant()) {
				course.setCourseStatus(1);
				courseExists.setRequiredApplicant(course.getRequiredApplicant());
				courseService.saveCourse(courseExists);
			}else if(courseExists.getRequiredApplicant() == course.getRequiredApplicant()) {
				mv.addObject("successMessage", "An update is irrelevant");	
			}else {
				bindingResult
					.rejectValue("courseName", "error.courseName",
							"There is already a course registered with the name provided update the "
							+ "subject if that's what you intend doing");
			}
			
			if(course.getSubjectModels().size() > 0) {				
				if(courseExists.getSubjectModels() != course.getSubjectModels()) {
					courseExists.setSubjectModels(course.getSubjectModels());
					courseService.saveCourse(courseExists);
				}
			}
		}
		mv.addObject("courseModel", new CourseModel());
		mv.addObject("subjectList", subjectService.getSubjects());		
		return mv;
	}
	
	@DeleteMapping("/courses")
	public void deleteCourse(HttpServletRequest request, HttpServletResponse response, CourseModel courseModel) {
		
		Enumeration<String> parameterNames = request.getParameterNames();
		
    	if(parameterNames.hasMoreElements()) {
    		if(Integer.parseInt(request.getParameter("courseStatus")) == 1) {
    			courseModel = courseService.findById(Integer.parseInt(request.getParameter("courseId")));
    			courseModel.setCourseStatus(0);
    			courseService.saveCourse(courseModel);
    		}
    	}
	}
	
	@PostMapping("/courses/fetch")
	@ResponseBody
	public String fetchCourse(HttpServletRequest request, HttpServletResponse response, CourseModel courseModel) {
		Enumeration<String> parameterNames = request.getParameterNames();
		
    	if(parameterNames.hasMoreElements()) {
    		courseModel = courseService.findById(Integer.parseInt(request.getParameter("courseId")));
		}
    	
    	Course course = new Course(courseModel.getCourseId(), courseModel.getCourseName(), courseModel.getRequiredApplicant());
    	return new Gson().toJson(course);
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/courses/paginated/")
	@ResponseBody
	public String listCoursesPaginated(HttpServletRequest request, HttpServletResponse response) {
		
		List<CourseModel> courseModelList = new ArrayList<>();
		List<Course> courseList = new ArrayList<>();
		DataTableResults<Course> dataTableResult = new DataTableResults<Course>();
		Enumeration<String> parameterNames = request.getParameterNames();	
		
    	if(parameterNames.hasMoreElements()) {    		
    		Query query = entityManager.createNativeQuery(
    				courseService.dataTableQuery(request), CourseModel.class);
    		courseModelList = query.getResultList();

    		Query recordCount = entityManager.createNativeQuery("SELECT * FROM CourseModel");
  
    		for(int i=0; i<courseModelList.size() ; i++) {
    			courseList.add(
					new Course(
							courseModelList.get(i).getCourseId(),
							courseModelList.get(i).getCourseName(),
							courseModelList.get(i).getCourseStatus(),
							courseModelList.get(i).getRequiredApplicant()
						)
					);
    		}
    		
    		for(Course course : courseList) {
    			
    			if(course.getCourseStatus() == 1) {
    				course.setValidStatus("<img src='/images/active.png' alt='Active' style='width:25px; height:25px;'/>");
    				course.setUpdate("<img src='/images/edit.png'  style='width:25px; height:25px;' name='update' class='update' id='"+course.getCourseId()+"' alt='Update'/>");
        			course.setDelete("<img src='/images/delete.png' style='width:25px; height:25px;' name='delete' class='delete' id='"+course.getCourseId()+"' data-status='"+course.getCourseStatus()+"' alt='Delete'>");
    			}else {
    				course.setValidStatus("<img src='/images/inactive.png' alt='Inactive' style='width:25px; height:25px;'/>");
    				course.setUpdate("<img src='/images/edit.png' style='width:25px; height:25px;' name='update' class='update' id='"+course.getCourseId()+"'  data-status='\"+subject.getSubjectStatus()+\"' alt='Delete'>");
        			course.setDelete("<img src='/images/delete.png' style='width:25px; height:25px;' name='delete' class='delete' id='"+course.getCourseId()+"' data-status='"+course.getCourseStatus()+"' alt='Delete'>");
    			}
    		}
    		
    		dataTableResult.setDraw(request.getParameter("draw").toString());
    		dataTableResult.setRecordsFiltered(Integer.toString(recordCount.getResultList().size()));
    		dataTableResult.setRecordsTotal(Integer.toString(courseList.size()));
    		dataTableResult.setListOfDataObjects(courseList);		
		}
		return new Gson().toJson(dataTableResult);
	}
}