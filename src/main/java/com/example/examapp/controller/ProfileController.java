package com.example.examapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.examapp.datatablemodel.Employee;
import com.example.examapp.datatablemodel.Student;
import com.example.examapp.model.EmployeeModel;
import com.example.examapp.model.StudentModel;
import com.example.examapp.model.SubjectModel;
import com.example.examapp.service.CourseService;
import com.example.examapp.service.EmployeeService;
import com.example.examapp.service.StudentService;
import com.example.examapp.service.SubjectService;
import com.google.gson.Gson;

@RestController
public class ProfileController {

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private SubjectService subjectService;

	@PersistenceContext
	private EntityManager entityManager;

	private Student student;
	private StudentModel studentModel;
	private Employee employee;
	private List<SubjectModel> subjectModelList;
	
	@GetMapping(value = "/profile/{id}")
	public ModelAndView studentProfile(ModelAndView mv, @PathVariable int id) {
		studentModel = studentService.findById(id);
		subjectModelList = new ArrayList<>();

		student = new Student(id, studentModel.getFirstName(), studentModel.getLastName(), studentModel.getOtherName(),
				studentModel.getEmail(), studentModel.getAddress(), studentModel.getPhone(), studentModel.getGender(),
				studentModel.getStatus(), studentModel.getOnline(), studentModel.getRegdate(), "",
				studentModel.getCourseModel().getCourseName().toString());

		subjectModelList = subjectService.subjectRegistered(studentModel.getCourseModel().getCourseId());

		for (int i = 1; i < subjectModelList.size(); i++) {
			subjectModelList.get(i).setSubjectId(i);
		}

		if (student.getStatus() == 1) {
			student.setValidStatus("<img src='/images/active.png' alt='Active'");
		} else {
			student.setValidStatus("<img src='/images/inactive.png' alt='Inactive'>");
		}

		student.setProfileImage("<img src='/Image/" + id + "' name='image' id='" + id
				+ "' alt='studentImage' class='img-rounded studentProfileImage'/>");

		mv.addObject("courseList", courseService.getCourses());
		mv.addObject("subjectList", subjectModelList);
		mv.addObject("student", student);
		mv.addObject("studentModel", new StudentModel());
		mv.setViewName("profile");
		return mv;
	}

	@PostMapping(value = "/profile")
	public RedirectView updateStudentDetail(@ModelAttribute("StudentModel") StudentModel studentModel,
			BindingResult bindingResult, ModelAndView mv) {

		StudentModel studentUpdate = studentService.findById(studentModel.getUserId());

		studentUpdate.setFirstName(studentModel.getFirstName());
		studentUpdate.setLastName(studentModel.getLastName());
		studentUpdate.setOtherName(studentModel.getOtherName());
		studentUpdate.setAddress(studentModel.getAddress());
		studentUpdate.setEmail(studentModel.getEmail());
		studentUpdate.setGender(studentModel.getGender());
		studentUpdate.setPhone(studentModel.getPhone());
		studentUpdate.setCourseModel(studentModel.getCourseModel());

		if (!bindingResult.hasErrors()) {
			studentService.updateStudent(studentUpdate);
			mv.addObject("successMessage", "Employee has been registered successfully");
		} else {
			bindingResult.rejectValue("email", "error.email", bindingResult.toString());
		}
		return new RedirectView("/profile/"+studentModel.getUserId());
	}
	
	@GetMapping(value = "/profile")
	public ModelAndView getemployeeProfile(ModelAndView mv) {
		
		String Username = SecurityContextHolder.getContext().getAuthentication().getName();
		EmployeeModel employeeModel = employeeService.findEmployeeEmail(Username);
		
		try {
			employee = new Employee(employeeModel.getUserId(), employeeModel.getFirstName(),
						employeeModel.getLastName(), employeeModel.getOtherName(),
						employeeModel.getEmail(), employeeModel.getStatus(),
						employeeModel.getOnline(), employeeModel.getRegdate(), "");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	
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

		employee.setProfileImage("<img src='/Image/" + employeeModel.getUserId() + "' name='image' id='" + employeeModel.getUserId()
				+ "' alt='employeeImage' class='img-rounded studentProfileImage'/>");

		mv.addObject("employee", employee);
		mv.addObject("employeeModel", new EmployeeModel());
		mv.setViewName("employeeprofile");
		return mv;
	}

	@GetMapping("/profile/fetch/{id}")
	public String fetchStudent(@PathVariable int id) {
		studentModel = studentService.findById(id);
		student = new Student(id, studentModel.getFirstName(), studentModel.getLastName(), studentModel.getOtherName(),
				studentModel.getEmail(), studentModel.getAddress(), studentModel.getPhone(), studentModel.getGender(),
				studentModel.getStatus(), studentModel.getOnline(), studentModel.getRegdate(), "",
				studentModel.getCourseModel().getCourseId().toString());
		// Persist ID
		student.setUserId(id);

		return new Gson().toJson(student);
	}
}