package com.example.examapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.examapp.model.ExamQuestion;
import com.example.examapp.model.ExamTimeFormat;
import com.example.examapp.model.QuestionProp;
import com.example.examapp.model.SaveResult;
import com.example.examapp.model.StudentModel;
import com.example.examapp.model.SubjectModel;
import com.example.examapp.service.EmailService;
import com.example.examapp.service.QuestionService;
import com.example.examapp.service.SettingsService;
import com.example.examapp.service.StudentService;
import com.example.examapp.service.SubjectService;
import com.google.gson.Gson;

@RestController
@Component
@Scope("application")
public class ExamController {

	@Autowired private QuestionService questionService;
	@Autowired private StudentService studentService;
	@Autowired private SubjectService subjectService;
	@Autowired private EmailService emailService;
	@Autowired private SettingsService settingsService;
		
	private List<String> subjectList;
	private List<String> buttonNavigationList;
	private List<SubjectModel> subjectModelList;	
	private List<ExamQuestion> listOfExamQuestions;	
	private StudentModel studentModel;
	private List<QuestionProp> questionProperty;
	private ExamTimeFormat examTimeFormat;
	
	private String Username = null;	
	private String profileImage = null;
	private String currentExamTime = "00:00:00";		
	private boolean endExam = false;
	private boolean startExam = false;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@GetMapping("/examquestion/{subjectId}/{questionNumber}")
	public ModelAndView getExamQuestions(ModelAndView mv, @PathVariable int subjectId, @PathVariable int questionNumber) {
		
		/**
		 * The Security context holder holds the details of the current logged in user
		 * The User name variable is assign the value of the current user's user name
		 * The Student model is assign
		 */
		try {	
			if(studentModel == null) {
				Username = SecurityContextHolder.getContext().getAuthentication().getName();
				if(Username ==null) {
					SecurityContextHolder.getContext().setAuthentication(null);
					mv.setViewName("/login");
					return mv;
				}
					
				studentModel = studentService.findStudentEmail(Username);				
				
				if(studentModel.getStatus() == 0) {
					SecurityContextHolder.getContext().setAuthentication(null);
					mv.setViewName("/login");
					return mv;
				}
				//Sets the students online status to true when logged for the first time
				studentModel.setOnline(1);				
				studentService.updateStudent(studentModel);
			}else {
				if(studentModel.getStatus() == 0) {
					SecurityContextHolder.getContext().setAuthentication(null);
					mv.setViewName("/login");
					return mv;
				}
			}
		}catch(Exception ex) {
			SecurityContextHolder.getContext().setAuthentication(null);
			mv.setViewName("/login");
			return mv;
		}	
	
		/**
		 * Sets the question number of a particular question
		 * If the question number is 6565 it means the subject is being visited for the first time
		 */				
		try {
			if(questionProperty!=null) {
				for(QuestionProp qp : questionProperty)
					if(qp.getSubjectId() == subjectId) {
						if(questionNumber == 6565) {		
							questionNumber = qp.getCurrentQuestionNumber();
						}else {
							qp.setCurrentQuestionNumber(questionNumber);
						}
						break;
					}
			}else {
				questionNumber = 0;
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		String subjectName = null;
		String Timer = null;
		
		try {
			buttonNavigationList = new ArrayList<>();
			subjectModelList = subjectService.subjectRegistered(studentModel.getCourseModel().getCourseId());
			subjectName = (String)subjectService.findById(subjectId).getSubjectName();
			Timer = "<h1>" + getCurrrentExamTime()+ "</h1>";
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		/**
		 * Sets the profile image of the student sitting for the exam
		 */
		profileImage = "<img src='/Image/" + studentModel.getUserId() + "' name='image' id='" + studentModel.getUserId() + "' alt='studentImage' class='img-rounded examProfileImage'/>";
	
		/**
		 * Retrieves questions from the database
		 * Assigns question to the listOfExamQuestions variable
		 */
		try {
			getExamQuestions();
		}catch(Exception ex) {
			System.out.println();
		}
		
		/**
		 * Initializes the list of Subject
		 * and sets the question properties
		 * if the subject list has not been assign
		 * 
		 * Initializes the exam time
		 */
		try {
			if(subjectList == null) {
				subjectList = new ArrayList<>();
				questionProperty = new ArrayList<>();				
				for(SubjectModel s : subjectModelList) {
					subjectList.add("<a href='/examquestion/"+s.getSubjectId()+"/6565' class='btn navigate-subject' id='"+s.getSubjectId()+"'>"+s.getSubjectName()+"</a>");
					questionProperty.add(new QuestionProp(s.getSubjectId(), s.getSubjectName(), listOfExamQuestions.size()));
				}
				subjectList.add("<a href='/endExam' class='btn btn-warning navigate-subject' id='submit'>Submit</a>");
				examTimeFormat = getTime(settingsService.getSetting().getSessionTime());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		/**
		 * Loads the question navigation buttons based on the current subject
		 */
		try {
			for(int i=0, x=0; i<listOfExamQuestions.size(); i++) {
				if(listOfExamQuestions.get(i).getQuestionSubject().equalsIgnoreCase(subjectName) ) {				
					if(listOfExamQuestions.get(i).isSelected()) {
						buttonNavigationList.add("<a href='/examquestion/"+subjectId+"/"+x+"' class='btn btn-success navigate' id='"+x+"'>"+ (x+1) +"</a>");
					}else {
						buttonNavigationList.add("<a href='/examquestion/"+subjectId+"/"+x+"' class='btn btn-warning navigate' id='"+x+"'>"+ (x+1) +"</a>");
					}
					x++;
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		try {
			mv.addObject("subjectList", subjectList);
			mv.addObject("buttonNavigationList", buttonNavigationList);
			mv.addObject("profileImage", profileImage);
			mv.addObject("subjectId", subjectId);
			mv.addObject("Timer", Timer);
			mv.addObject("maxQuestion", settingsService.getSetting().getNumberOfQuestion());
			mv.addObject("examQuestion", getQuestionBySubject(listOfExamQuestions, subjectId, questionNumber));
			
			if(getQuestionBySubject(listOfExamQuestions, subjectId, questionNumber).getOptionSelected() != null) {
				mv.addObject("optionSelected", getQuestionBySubject(listOfExamQuestions, subjectId, questionNumber).getOptionSelected().toUpperCase());
			}else {
				mv.addObject("optionSelected", "");
			}
			mv.setViewName("exam");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		startExam = true;
		return mv;
	}
	
	@PostMapping("/examquestion/{subjectId}/{questionNumber}")
	public RedirectView getExamQuestio(ModelAndView mv, @PathVariable int subjectId, @PathVariable int questionNumber) {
		
		return new RedirectView("/login");
	}
	
	/**
	 *This method aims to retrieve a random list of questions from the database
	 */
	public List<ExamQuestion> getExamQuestions(){
		
		try {
			if(listOfExamQuestions == null) {
				listOfExamQuestions = questionService.getExamQuestion(subjectModelList, settingsService.getSetting().getNumberOfQuestion());
				System.out.println("Question list initialized");
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return listOfExamQuestions;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/examquestion/{studentId}")
	public List<ExamQuestion> getExamQuestion(@PathVariable int studentId) {
		
		List<SubjectModel> subjectModelList = new ArrayList<>();
		String courseId = studentService.getCourseId(studentId);
		
		if(!courseId.isEmpty()) {
			
			String queryString = "SELECT s.subjectId, s.subjectName, s.subjectStatus " + "FROM SubjectModel s "
					+ "INNER JOIN CourseSubject cs " + "ON s.subjectId = cs.subjectId " + "WHERE cs.courseId = " + courseId;
			
			Query query = entityManager.createNativeQuery(queryString, SubjectModel.class);
			
			subjectModelList = query.getResultList();
		}
		
		return questionService.getExamQuestion(subjectModelList, settingsService.getSetting().getNumberOfQuestion());
	}
	
	/**
	 *This method aims to retrieve a question from a list of questions being answered by the student
	 */
	public ExamQuestion getQuestionBySubject(List<ExamQuestion> questionList, int subjectId, int questionNumber){
		
		ExamQuestion question = new ExamQuestion();
		try {
			String subjectName = (String)subjectService.findById(subjectId).getSubjectName();
			for(ExamQuestion s :  questionList) 
				if(s.getQuestionSubject().equalsIgnoreCase(subjectName) && s.getQuestionNumber() == (questionNumber+1))
						question = s;
					for(QuestionProp qp :  questionProperty)
						if(qp.getSubjectId() == subjectId)
								qp.setCurrentQuestionNumber(questionNumber);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return question;
	}

	/**
	 * This method aims to set the method selected by a student user
	 * if a student is found to have already sat for the test he is redirected to the login page
	 * loops through the list of exam questions
	 * it finds the subject whose option is to be selected 
	 * Sets the option
	 * redirects user to the exam page
	 */
	@GetMapping("/examoption/{subjectId}/{questionNumber}/{option}")
	public RedirectView setSelectedOption(@PathVariable int subjectId, @PathVariable int questionNumber, @PathVariable String option) {

		try {
			if(studentModel.getTakenTest() == 1) {
				return new RedirectView("/logout");
			}
			
			String subjectName = (String)subjectService.findById(subjectId).getSubjectName();		
			try {
				for(ExamQuestion s : listOfExamQuestions)
					if(s.getQuestionSubject().equalsIgnoreCase(subjectName) && s.getQuestionNumber() == (questionNumber+1)) {
						switch(option) {
							case "optionA":
								s.setOptionSelected("A");
								break;
							case "optionB":
								s.setOptionSelected("B");
								break;
							case "optionC":
								s.setOptionSelected("C");
								break;
							case "optionD":
								s.setOptionSelected("D");
								break;
						}
						s.setSelected(true);
					}	
			}catch(Exception ex) {
				System.out.println(ex.getMessage());
			}			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return new RedirectView("/examquestion/"+subjectId+"/"+questionNumber+"/"+option);
	}
	
	/**
	 * Returns a String value of the current exam time
	 */
	@GetMapping("/getExamTime")
	public String getCurrrentExamTime() {
		String Timer = currentExamTime;
		return Timer;
	}
	
	/**
	 *this method is called by the submit button on the exam page
	 *it updates the database to reflect that the student has sat for the test
	 *returns the result model
	 */
	@GetMapping("/endExam")
	public ModelAndView Submit(ModelAndView mv) {
		try {
			studentModel.setTakenTest(1);
			studentModel.setStudentScore(questionService.getStudentScore(listOfExamQuestions));
			studentModel.setOnline(0);
			studentModel.setStatus(0);
			studentService.updateStudent(studentModel);
			questionProperty = questionService.getResult(listOfExamQuestions, questionProperty);
			//emailService.sendSimpleEmail(questionProperty, studentModel);
			endExam = false;
			startExam = false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}finally {
			mv.setViewName("result");
		}
		return mv;
	}
	
	/**
	 *Returns the result of the exam taken by the student
	 *It checks if the list of exam questions is not empty
	 *If not empty it returns the result of the test taken;
	 */
	@GetMapping("/getResult")
	public String  getResult(ModelAndView mv) {	
		/**
		 * Ensures that the student has sat for the exam
		 * "No Result" is return if the student has sat for the exam
		 */
		try {
			if(studentModel != null) {
				if(studentModel.getTakenTest() == 0) {
					return "No Result";
				}
			}else {
				return "No Result";
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		/**
		 * Sets the studentModel to null
		 */
		studentModel = null;
		
		if(listOfExamQuestions != null)
        	if(questionProperty.get(0).getTotalScore() == 0)
        		return new Gson().toJson(questionService.getResult(listOfExamQuestions, questionProperty));
        	else return new Gson().toJson(questionProperty);
		else return "No Result";
	}
	
	/**
	 * This acts as the timer that update the time per second and sets currentExamTime variable to the current time
	 * When the time for the exam elapses, it submits the exam automatically
	 */
	@Scheduled(fixedRate = 1000, initialDelay = 0)
	public void cronJobSch() {
		if(startExam && !endExam) {
			if(examTimeFormat.getHH() != 0 || examTimeFormat.getMM() != 0 || examTimeFormat.getSS() != 0) {
		        if(examTimeFormat.getSS() == 0){
		        	examTimeFormat.setSS(59);
		            if(examTimeFormat.getMM() == 0){
			        	examTimeFormat.setMM(59);
		                if(examTimeFormat.getHH() != 0){
		                	examTimeFormat.setHH(examTimeFormat.getHH()-1);
		                }
		            }else{
		            	examTimeFormat.setMM(examTimeFormat.getMM()-1);
		            }
		        }else{
		        	examTimeFormat.setSS(examTimeFormat.getSS()-1);
		        }
			}else {
				try {
					if(endExam) {
						studentModel.setTakenTest(1);
						studentModel.setStudentScore(questionService.getStudentScore(listOfExamQuestions));
						studentModel.setOnline(0);
						studentModel.setStatus(0);
						studentService.updateStudent(studentModel);
						questionProperty = questionService.getResult(listOfExamQuestions, questionProperty);
						emailService.sendSimpleEmail(questionProperty, studentModel);
						endExam = false;
						startExam = false;					}
				}catch(Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		
        	currentExamTime =  stringCountdown(examTimeFormat.getHH(),examTimeFormat.getMM(),examTimeFormat.getSS());
		}
	}
	
	/**
	 * Return a standard timer formated string
	 */
	private String stringCountdown(int hh, int mm, int ss) {
        String H = "", M = "", S = "";
        try {
	        if (hh < 10) {
	            H = "0" + hh;
	        } else {
	            H = String.valueOf(hh);
	        }
	        if (mm < 10) {
	            M = "0" + mm;
	        } else {
	            M = String.valueOf(mm);
	        }
	        if (ss < 10) {
	            S = "0" + ss;
	        } else {
	            S = String.valueOf(ss);
	        }
        }catch(Exception ex) {
        	System.out.println();
        }
        return H + ":" + M + ":" + S;
    }
	
	public static ExamTimeFormat getTime(int timeInMinutes) {
		
		if(timeInMinutes > 0) {
			if(timeInMinutes < 60) {
				return new ExamTimeFormat(0,timeInMinutes,0);
			}else {
				return new ExamTimeFormat(timeInMinutes/60, timeInMinutes % 60, 0);
			}
		}else {
			return new ExamTimeFormat(0,1,0);
		}
	}
	
	/**
	 * API
	 */
	@PostMapping("/saveResult")
	@ResponseBody
	public String saveResult(@RequestBody SaveResult saveResult) {
		
		StudentModel student =  studentService.findById(saveResult.getId());
		int totalScore = 0;
		
		for(QuestionProp qp : saveResult.getQuestionProperty()) {
			totalScore += qp.getTotalScore();
		}
		
		student.setTakenTest(1);
		student.setStudentScore(totalScore);
		student.setOnline(0);
		student.setStatus(0);
		//studentService.updateStudent(student);

		emailService.sendSimpleEmail(saveResult.getQuestionProperty(), student);

		return new Gson().toJson("Result saved");
	}
}