package com.example.examapp.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.datatablemodel.Student;
import com.example.examapp.model.StudentModel;
import com.example.examapp.model.SubjectModel;
import com.example.examapp.service.CourseService;
import com.example.examapp.service.StudentService;
import com.example.examapp.service.SubjectService;
import com.google.gson.Gson;

@RestController
public class StudentProfileController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private SubjectService subjectService;

	@PersistenceContext
	private EntityManager entityManager;

	private Student student;
	private StudentModel studentModel;
	private List<SubjectModel> subjectModelList;
	
	@GetMapping(value = "/studentprofile/{id}")
	public ModelAndView studentProfile(ModelAndView mv, @PathVariable int id) {
		studentModel = studentService.findById(id);
		subjectModelList = new ArrayList<>();

		System.out.println(studentModel.getPhone() + "  ----------  "+ studentModel.getGender());
		student = new Student(id, studentModel.getFirstName(), studentModel.getLastName(), studentModel.getOtherName(),
				studentModel.getEmail(), studentModel.getAddress(), studentModel.getPhone(), studentModel.getGender(),
				studentModel.getStatus(), studentModel.getOnline(), studentModel.getRegdate(), "",
				studentModel.getCourseModel().getCourseName().toString());

		subjectModelList = subjectService.subjectRegistered(studentModel.getCourseModel().getCourseId());
		
		for (int i = 1; i < subjectModelList.size(); i++) {
			subjectModelList.get(i).setSubjectId(i);
		}

		if (student.getStatus() == 1) {
			student.setValidStatus("<img src='/images/active.png' alt='Active' style='width:30px; height:30px;' />");
		} else {
			student.setValidStatus("<img src='/images/inactive.png' alt='Inactive' style='width:30px; height:30px;' />");
		}
		
		if(student.getOnline() == 1) {
			student.setOnlineStatus("<img src='/images/Online.png' alt='Active' style='width:30px; height:30px;'/>");
		}else {
			student.setOnlineStatus("<img src='/images/offline.png' alt='Inactive' style='width:30px; height:30px;' />");
		}
		
		student.setAddress(studentModel.getAddress());
		student.setProfileImage("<img src='/Image/" + id + "' name='image' id='" + id
				+ "' alt='studentImage' class='img-rounded userProfileImage'/>");

		mv.addObject("courseList", courseService.getCourses());
		mv.addObject("subjectList", subjectModelList);
		mv.addObject("student", student);
		mv.addObject("studentModel", new StudentModel());
		mv.setViewName("studentprofile");
		return mv;
	}

	@PostMapping(value = "/studentprofile")
	public String updateStudentDetail(HttpServletRequest request, StudentModel studentUpdate) {
		
		Enumeration<String> parameterNames = request.getParameterNames();

    	if(parameterNames.hasMoreElements()) {
    		
    		studentUpdate = studentService.findById(Integer.parseInt(request.getParameter("userId")));
    		studentUpdate.setFirstName(String.valueOf(request.getParameter("firstName")));
    		studentUpdate.setLastName(String.valueOf(request.getParameter("lastName")));
    		studentUpdate.setOtherName(String.valueOf(request.getParameter("otherName")));
    		studentUpdate.setEmail(String.valueOf(request.getParameter("email")));
    		studentUpdate.setFirstName(String.valueOf(request.getParameter("address")));
    		studentUpdate.setLastName(String.valueOf(request.getParameter("email")));
    		studentUpdate.setOtherName(String.valueOf(request.getParameter("gender")));
    		studentUpdate.setEmail(String.valueOf(request.getParameter("phone")));
    		studentUpdate.setCourseModel(courseService.findById(Integer.valueOf(request.getParameter("courseModel"))));
    		
    		studentService.updateStudent(studentUpdate);    		
    	}
		String msg = "Update Successful";
		return new Gson().toJson(msg);
	}

		//studentUpdate.setCourseModel(studentModel.getCourseModel());

	@GetMapping("/studentprofile/fetch/{id}")
	@ResponseBody
	public String fetchStudent(@PathVariable int id) {
		studentModel = studentService.findById(id);
		student = new Student(id, studentModel.getFirstName(), studentModel.getLastName(), studentModel.getOtherName(),
				studentModel.getEmail(), studentModel.getAddress(), studentModel.getPhone(), studentModel.getGender(),
				studentModel.getStatus(), studentModel.getOnline(), studentModel.getRegdate(), "",
				studentModel.getCourseModel().getCourseId().toString());
		// Persist ID
		//student.setUserId(id);
		return new Gson().toJson(student);
	}
}
