package com.example.examapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.examapp.model.ExamQuestion;
import com.example.examapp.model.QuestionModel;
import com.example.examapp.model.QuestionProp;
import com.example.examapp.model.SubjectModel;
import com.example.examapp.repository.QuestionModelRepo;

@Service
public class QuestionService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	QuestionModelRepo questionRepo;
	
	public List<QuestionModel> getQuestions() {
		return questionRepo.findAll();
	}
	public QuestionModel findByQuestion(String questionName) {
		return questionRepo.findByQuestionName(questionName);
	}
	public void saveQuestion(@Valid QuestionModel questionModel) {
		questionRepo.save(questionModel);
	}
	
	public void deletebyId(int studentId) {
		questionRepo.deleteById(studentId);
	}
	
	public String dataTableQuery(HttpServletRequest request) {
		String query = "";
		query = "SELECT * FROM QuestionModel ";
		
		if(request.getParameter("search[value]").length() > 0)
		{
			query += "WHERE questionName LIKE '%" + request.getParameter("search[value]") + "%' ";
			query += "OR questionId LIKE '%" + request.getParameter("search[value]") +"%' ";
		}			
		query += " ORDER BY "+request.getParameter("columns["+ request.getParameter("order[0][column]") +"][name]")
		+ " " + request.getParameter("order[0][dir]")+" ";
		
		if(Integer.parseInt(request.getParameter("length")) !=-1)
		{
			query += " LIMIT " + request.getParameter("start") + ", " + request.getParameter("length");
		}
		return query;
	}
	
	public QuestionModel findById(int questionId) {
		return questionRepo.findById(questionId);
	}
	
	public List<ExamQuestion> getExamQuestion(List<SubjectModel> subject, int numberOfQuestions){
		List<ExamQuestion> examQuestion = new ArrayList<>();
		String queryString;
		QuestionModel questionExists;
		Query query = null;
		int totalRecords = 0;
		for(int x=0; x < subject.size();) {
			int min=0, max=0;
			/**
			 * This query aims to derive the range of IDs of a particular subject id.
			 */
			queryString= "SELECT MIN(questionId) mini FROM QuestionModel q WHERE subjectId = "
												+ subject.get(x).getSubjectId()+" LIMIT 1";
			
			query = entityManager.createNativeQuery(queryString);
			if(query.getResultList().get(0) != null) min = (int)query.getResultList().get(0);
			
			queryString= "SELECT MAX(questionId) maxi FROM QuestionModel q WHERE subjectId = "
					+ subject.get(x).getSubjectId()+" LIMIT 1";
			
			query = entityManager.createNativeQuery(queryString);
			if(query.getResultList().get(0) != null) max = (int)query.getResultList().get(0);
			
			queryString= "SELECT COUNT(questionId) FROM QuestionModel WHERE subjectId = "
					+ subject.get(x).getSubjectId()+" LIMIT 1";
			
			query = entityManager.createNativeQuery(queryString);
			if(query.getResultList().get(0) != null) totalRecords = Integer.parseInt(query.getResultList().get(0).toString());
			
			if(totalRecords < numberOfQuestions) numberOfQuestions = totalRecords;
			
			if(min >0 && max > 0 ) {
				if(max > min) {
					boolean matchFound = false;
					int randomNumber;
			
					for(int i=0; i<numberOfQuestions;) {
						matchFound = false;
						randomNumber = ThreadLocalRandom.current().nextInt( min, max+1);
						questionExists = questionRepo.findById(randomNumber);
						
						if(questionExists != null && questionExists.getSubjectModel().getSubjectName() == subject.get(x).getSubjectName()) {
							if (i ==0) setExamQuestion(examQuestion, questionExists, ++i);
							else {
								for(ExamQuestion e : examQuestion) {
									if(e.getQuestionId() == randomNumber)	{
										matchFound = true;
										break;
									}
								}
								if(!matchFound) setExamQuestion(examQuestion, questionExists, ++i);
							}
						}
						if(i == numberOfQuestions) x++;
					}
				}
			}else {
				x++;
			}
		}
		return examQuestion;
	}
	
	private void setExamQuestion(List<ExamQuestion> eq, QuestionModel q, int questionNumber) {
		eq.add(new ExamQuestion(
					q.getQuestionId(), q.getQuestionName(), questionNumber, q.getOptionA(), q.getOptionB(),
					q.getOptionC(), q.getOptionD(), q.getQuestionAnswer(), q.getSubjectModel().getSubjectName(),"",false
				));
	}
	
	public List<QuestionProp> getResult(List<ExamQuestion> examQuestion, List<QuestionProp> questionProp){
		for(ExamQuestion e : examQuestion) {
			if(e.getAnswer().equalsIgnoreCase(e.getOptionSelected())) {
				for(QuestionProp qp : questionProp) {
					if(e.getQuestionSubject().equalsIgnoreCase(qp.getSubjectName())) {
						qp.setTotalScore(qp.getTotalScore() + 1);
					}
				}
			}				
		}		
		return questionProp;
	}
	
	public int getStudentScore(List<ExamQuestion> examQuestion) {
		int total = 0;
		
		for(ExamQuestion e : examQuestion) {
			if(e.getAnswer().equalsIgnoreCase(e.getOptionSelected())) {
				total ++;
			}				
		}
		return total;
	}
}
