package com.example.examapp.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.examapp.model.SubjectModel;
import com.example.examapp.repository.SubjectModelRepo;

@Service
public class SubjectService {

	@Autowired
	private SubjectModelRepo subjectRepo;
	
	@PersistenceContext
	private EntityManager entityManager;

    public SubjectModel findBySubject(String subject_name) {
		return subjectRepo.findBySubjectName(subject_name);
	}
    
    public SubjectModel findById(int subjectId) {
		return subjectRepo.findById(subjectId);
	}
    
	public void saveSubject(SubjectModel subject) {
		subjectRepo.save(subject);
	}
	
	public List<SubjectModel> getSubjects(){
		return subjectRepo.findAll();
	}
	
	public void deleteSubject(int id) {
		subjectRepo.deleteSubject(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<SubjectModel> subjectRegistered(int courseId){
		String queryString = "SELECT s.subjectId, s.subjectName, s.subjectStatus " + "FROM SubjectModel s "
				+ "INNER JOIN CourseSubject cs " + "ON s.subjectId = cs.subjectId " + "WHERE cs.courseId = " + courseId;
		Query query = entityManager.createNativeQuery(queryString, SubjectModel.class);
		List<SubjectModel> subjectList = new ArrayList<>();
		subjectList = query.getResultList();
		return subjectList;
	}
	
	public String dataTableQuery(HttpServletRequest request) {
		String query = "";
		query += "SELECT * FROM SubjectModel ";	
		
		if(request.getParameter("search[value]").length() > 0)
		{
			query += "WHERE subjectName LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR subjectStatus LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR subjectId LIKE '%" + request.getParameter("search[value]") +"%' ";
		}
		query += "ORDER BY "+request.getParameter("columns["+ request.getParameter("order[0][column]") +"][name]")
		+ " " + request.getParameter("order[0][dir]")+" ";
		
		if(Integer.parseInt(request.getParameter("length")) !=-1)
		{
			query += "LIMIT " + request.getParameter("start") + ", " + request.getParameter("length");
		}
		return query;
	}
}
