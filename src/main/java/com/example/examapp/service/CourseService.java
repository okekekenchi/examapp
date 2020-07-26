package com.example.examapp.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.examapp.model.CourseModel;
import com.example.examapp.repository.CourseModelRepo;

@Service
public class CourseService {

	@Autowired
	private CourseModelRepo courseRepo;
	
	@PersistenceContext
	private EntityManager entityManager;

    public CourseModel findByCourse(String course_name) {
		return courseRepo.findByCourseName(course_name);
	}

	public void saveCourse(CourseModel course) {
        courseRepo.save(course);
	}

	public List<CourseModel> getCourses() {
		return courseRepo.findAll();
	}
	
	@SuppressWarnings("unchecked")
	public List<CourseModel> getApprovedCourses() {		
		String queryString = "SELECT distinct (c.courseId) , c.courseName, c.courseStatus, c.requiredApplicant " + 
		 		"FROM courseModel c " + 
		 		"INNER JOIN coursesubject cs on c.courseId = cs.courseId";
		
		Query query = entityManager.createNativeQuery(queryString, CourseModel.class);
		List<CourseModel> courseList = new ArrayList<>();
		courseList = query.getResultList();
		return courseList;
	}
	
	public String dataTableQuery(HttpServletRequest request) {
		String query = "";
		query += "SELECT  courseId, courseName, requiredApplicant, courseStatus FROM CourseModel  ";	
		
		if(request.getParameter("search[value]").length() > 0)
		{
			query += "WHERE courseName LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR requiredApplicant LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR courseStatus LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR courseId LIKE '%" + request.getParameter("search[value]") +"%' ";
		}
		query += "ORDER BY "+request.getParameter("columns["+ request.getParameter("order[0][column]") +"][name]")
		+ " " + request.getParameter("order[0][dir]")+" ";
		
		if(Integer.parseInt(request.getParameter("length")) !=-1)
		{
			query += "LIMIT " + request.getParameter("start") + ", " + request.getParameter("length");
		}
		return query;
	}

	public CourseModel findById(int courseId) {
		return courseRepo.findById(courseId);
	}
}
