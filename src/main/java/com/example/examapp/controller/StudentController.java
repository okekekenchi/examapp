package com.example.examapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.datatablemodel.Student;
import com.example.examapp.model.StudentModel;
import com.example.examapp.pagination.DataTableResults;
import com.example.examapp.service.CourseService;
import com.example.examapp.service.StudentService;
import com.google.gson.Gson;

@RestController
public class StudentController {
	@Autowired
	private StudentService studentService;

	@Autowired
	private CourseService courseService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@GetMapping(value="/result")
	public ModelAndView result(ModelAndView mv){
		return mv;
	}
	
	@GetMapping(value="/student")
	public ModelAndView registration(ModelAndView mv){
		mv.addObject("courseList", courseService.getCourses());
		mv.addObject("studentModel", new StudentModel());
		return mv;
	}

	@DeleteMapping("/student")
	public void deleteStudent(HttpServletRequest request) {
		StudentModel studentModel = new StudentModel();
		Enumeration<String> parameterNames = request.getParameterNames();

    	if(parameterNames.hasMoreElements()) {
    		if(Integer.parseInt(request.getParameter("studentStatus")) == 1) {
    			studentModel = studentService.findById(Integer.parseInt(request.getParameter("studentId")));
    			studentModel.setStatus(0);
    			studentService.saveStudent(studentModel);
    		}
    	}
	}
	
	@PostMapping("student/logout")
	public void logoutStudent(HttpServletRequest request) {
		StudentModel studentModel = new StudentModel();
		Enumeration<String> parameterNames = request.getParameterNames();

    	if(parameterNames.hasMoreElements()) {
			studentModel = studentService.findById(Integer.parseInt(request.getParameter("studentId")));
			studentModel.setOnline(0);
			studentService.saveStudent(studentModel);
    	}
    	System.out.println(request.getParameter("studentId"));
	}
	
	
	@PostMapping("/student/view")
	public String fetchStudentDetail(HttpServletRequest request, HttpServletResponse response, StudentModel studentModel) {
		Enumeration<String> parameterNames = request.getParameterNames();
    	String studentDetail = "";
    	if(parameterNames.hasMoreElements()) {
			studentModel = studentService.findById(Integer.parseInt(request.getParameter("studentId")));
			
			studentDetail += " <div class='table-responsive'> "+ 
							"<table class='table table-boredered'> "+
								"<tr> "+
										"<td>First Name</td> "+
										"<td>"+studentModel.getFirstName()+"</td> "+
								"</tr> "+
								"<tr> "+
									"<td>Last Name</td> "+
									"<td>"+studentModel.getLastName()+"</td> "+
								"</tr> ";
									if(!studentModel.getOtherName().isEmpty()) {
					studentDetail +=	"<tr> "+
											"<td>Other Name</td> "+
											"<td>"+studentModel.getOtherName()+"</td> "+
										"</tr> ";
									}
				studentDetail +="<tr> "+
									"<td>Email</td> "+
									"<td>"+studentModel.getEmail()+"</td> "+
								"</tr> "+
								"<tr> "+
									"<td>Phone</td> "+
									"<td>"+studentModel.getPhone()+"</td> "+
								"</tr> "+
								"<tr> "+
									"<td>Address</td> "+
									"<td>"+studentModel.getAddress()+"</td> "+
								"</tr> "+
								"<tr> "+
									"<td>Gender</td> "+
									"<td>"+studentModel.getGender()+"</td> "+
								"</tr> "+
								"<tr> "+
									"<td>Registration Date</td> "+
									"<td>"+studentModel.getRegdate()+"</td> "+
								"</tr> "+
								"<tr> "+
									"<td>Examination Date</td> ";
									if(studentModel.getExamDate() != null) {
										studentDetail += "<td>"+studentModel.getExamDate()+"</td> ";
									}else {
										studentDetail += "<td>Date not assigned yet</td> ";
									}
			studentDetail +=   "</tr> "+
							"</table>" + 
						"</div>";					
		}
    	return studentDetail;
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/student/paginated/")
	public String listStudentsPaginated(HttpServletRequest request, HttpServletResponse response)
						throws ServletException, IOException{
		
		List<StudentModel> studentModelList = new ArrayList<>();
		List<Student> studentList = new ArrayList<>();
		DataTableResults<Student> dataTableResult = new DataTableResults<Student>();
		Enumeration<String> parameterNames = request.getParameterNames();
		
    	if(parameterNames.hasMoreElements()) {
    		Query query = entityManager.createNativeQuery(
    				studentService.dataTableQuery(request), StudentModel.class);
    		studentModelList = query.getResultList();

    		Query recordCount = entityManager.createNativeQuery("SELECT * FROM StudentModel");
    		    		
    		for(int i=0; i<studentModelList.size() ; i++) {
    			studentList.add(
					new Student(
							studentModelList.get(i).getUserId(),
							studentModelList.get(i).getFirstName(),
							studentModelList.get(i).getLastName(),
							studentModelList.get(i).getOtherName(),
							studentModelList.get(i).getEmail(),
							studentModelList.get(i).getStatus(),
							studentModelList.get(i).getOnline(),
							studentModelList.get(i).getCourseModel().getCourseName(),
							""
							)
					);
    		}
    		
    		for(Student student : studentList) {
    			if(student.getStatus() == 1) {
    				student.setValidStatus("<img src='/images/active.png' alt='Active' style='width:25px; height:25px;'/>");
    				student.setDelete("<img src='/images/delete.png'  style='width:25px; height:25px;' name='delete' class='delete' id='"+student.getUserId()+"' data-status='"+student.getStatus()+"' alt='Delete'>");
    			}else {
    				student.setValidStatus("<img src='/images/inactive.png' alt='Inactive' style='width:25px; height:25px;'/>");
    				student.setDelete("<img src='/images/delete.png' onclick='return;' style='width:25px; height:25px;' name='delete' class='delete' id='"+student.getUserId()+"' data-status='"+student.getStatus()+"' alt='Delete'>");
    			}
    			
    			if(student.getOnline() == 1) {
    				student.setOnlineStatus("<img src='/images/Online.png' alt='Active' style='width:25px; height:25px;'/>");
    				student.setLogout("<img src='/images/logout1.png'  style='width:25px; height:25px;' name='logout' class='logout' id='"+student.getUserId()+"' alt='Logout'>");
    			}else {
    				student.setOnlineStatus("<img src='/images/offline.png' alt='Inactive' style='width:25px; height:25px;' />");
    				student.setLogout("<img src='/images/logout1.png' onclick='return;' style='width:25px; height:25px;' name='logout' class='logout' id='"+student.getUserId()+"' alt='Logout'>");
    			}
			    			
    			student.setView("<img src='/images/view.png' name='view' id='"+student.getUserId()+"' class='view' alt='View' style='width:25px; height:25px;'>");
    								
    			student.setProfileImage("<a href='/employeeprofile/"+student.getUserId()+"' ><img src='/images/edit.png' name='image' id='"+student.getUserId()+"' alt='employeeImage' style='zoom:1'/></a>");
    		}
    		
    		dataTableResult.setDraw(request.getParameter("draw").toString());
    		dataTableResult.setRecordsFiltered(Integer.toString(recordCount.getResultList().size()));
    		dataTableResult.setRecordsTotal(Integer.toString(studentList.size()));
    		dataTableResult.setListOfDataObjects(studentList);		
		}
		return new Gson().toJson(dataTableResult);
	}
	
	/**
	 * Returns the total number of students applying in the most current year.
	 */
	
	@GetMapping("/nRegisteredStudents")
	public int numberOfStudents() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		
		Query recordCount = entityManager.createNativeQuery("SELECT count(studentId) "+
															"FROM studentmodel s "+
															"INNER JOIN usermodel u "+
															"ON s.studentId = u.userId "+
															"WHERE YEAR(u.regDate) = '"+ year +"';"
														);
		
		return Integer.valueOf(recordCount.getResultList().get(0).toString());
	}
}