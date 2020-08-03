package com.example.examapp.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.examapp.model.RoleModel;
import com.example.examapp.model.StudentModel;
import com.example.examapp.repository.RoleModelRepo;
import com.example.examapp.repository.StudentModelRepo;

@Service
public class StudentService {

	@Autowired
	private StudentModelRepo studentRepo;
	
	@Autowired
    private RoleModelRepo roleRepo;
	
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @PersistenceContext
	private EntityManager entityManager;
             
    public StudentModel findStudentEmail(String email) {
		return studentRepo.findByEmail(email);
	}
    
	public void saveStudent(StudentModel student) {
		try {
			student.setPassword(bCryptPasswordEncoder.encode(student.getPassword()));
	        student.setOnline(0);
	        RoleModel studentRole = roleRepo.findByRoleName("STUDENT");
	        student.setRoleName(new HashSet<RoleModel>(Arrays.asList(studentRole)));
	        studentRepo.save(student);
		}catch(Exception e) {
			System.out.println("Error");
		}
	}

	public void updateStudent(StudentModel student) {
		try {
	        studentRepo.save(student);
		}catch(Exception e) {
			System.out.println("Error");
		}
	}

	public List<StudentModel> getStudents() {
		return studentRepo.findAll();
	}
	
	public String dataTableQuery(HttpServletRequest request) {
		String query = "";
		query += "SELECT DISTINCT s.studentId, s.examDate, u.firstName, u.lastName, u.image, u.otherName, u.status, "+
				"u.email, r.roleName, u.online, s.address, s.phone, s.gender, "+
				"u.password, u.regdate, s.studentScore, s.takenTest, c.courseName, c.courseId "+
				"FROM UserModel u "+
				"INNER JOIN studentmodel s on u.userid = s.studentid "+
				"INNER JOIN userrole ur on u.userid = ur.userId "+
				"INNER JOIN rolemodel r on ur.roleId = r.roleId "+
				"INNER JOIN coursesubject cs on s.courseId = cs.courseId "+
				"INNER JOIN coursemodel c on cs.courseId = c.courseId ";
		
		if(request.getParameter("search[value]").length() > 0)
		{
			query += "WHERE s.studentId LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR u.firstName LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR u.lastName LIKE '%" + request.getParameter("search[value]") +"%' ";
			query += "OR u.otherName LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR u.status LIKE '%" + request.getParameter("search[value]") +"%' ";
			query += "OR u.address LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR u.email LIKE '%" + request.getParameter("search[value]") +"%' ";
			query += "OR c.courseName LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR s.studentScore LIKE '%" + request.getParameter("search[value]") +"%' ";
			query += "OR u.phone LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR u.online LIKE '%" + request.getParameter("search[value]") +"%' ";
			query += "OR u.gender LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR u.regdate LIKE '%" + request.getParameter("search[value]") + "%' ";
		}			
		query += "ORDER BY "+request.getParameter("columns["+ request.getParameter("order[0][column]") +"][name]")
		+ " " + request.getParameter("order[0][dir]")+" ";
		
		if(Integer.parseInt(request.getParameter("length")) !=-1)
		{
			query += "LIMIT " + request.getParameter("start") + ", " + request.getParameter("length");
		}
		return query;
	}

	public StudentModel findById(int studentId) {
		return studentRepo.findById(studentId);
	}
	
	public String getCourseId(int studentId) {
		String queryString = "SELECT courseId FROM StudentModel" + 
							" WHERE studentId = " + studentId;

		Query query = entityManager.createNativeQuery(queryString);
		
		return query.getResultList().get(0).toString();
	}
	
	public List<StudentModel> getRegisteredStudents() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		
		return studentRepo.findRegisteredStudents(year);
	}
	
	public int getActiveStudents() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		
		return studentRepo.nActiveStudents(year);
	}
	
	public int getStudentsOnline() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		
		return studentRepo.nStudentsOnline(year);
	}
}