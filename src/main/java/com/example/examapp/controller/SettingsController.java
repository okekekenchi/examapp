package com.example.examapp.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.model.SettingsModel;
import com.example.examapp.model.StudentModel;
import com.example.examapp.service.SettingsService;
import com.example.examapp.service.StudentService;
import com.google.gson.Gson;

@RestController
public class SettingsController {

	@Autowired
	private SettingsService settingsService;
	
	@Autowired
	private StudentService studentService;
	
	@GetMapping(value="/settings")
	public ModelAndView getSettingsPage(ModelAndView mv) {
		mv.setViewName("/settings");
		return mv;
	}
	
	@PostMapping(value="/settings")
	public String saveSettings(HttpServletRequest request) {
		Enumeration<String> parameterNames = request.getParameterNames();
		SettingsModel settingsModel = new SettingsModel();
    	try {
	    	if(parameterNames.hasMoreElements()) {
				settingsModel = settingsService.getSettings(Integer.parseInt(request.getParameter("settingsId")));
				settingsModel.setNComputers(Integer.parseInt(request.getParameter("nComputers")));
			
			    Date date = (new SimpleDateFormat("MM/dd/yyyy")).parse(String.valueOf(request.getParameter("examStartDate")));
				settingsModel.setExamStartDate(date);
				settingsModel.setExamStartTime(String.valueOf(request.getParameter("examStartTime")));
				settingsModel.setExamEndTime(String.valueOf(request.getParameter("examEndTime")));
				settingsModel.setSessionTime(Integer.parseInt(request.getParameter("sessionTime")));
				settingsModel.setNumberOfQuestion(Integer.parseInt(request.getParameter("nQuestion")));
			}
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage()+" error");
    	}finally {
    		settingsService.Save(settingsModel);
    	}
		return new Gson().toJson(settingsService.getSettings().get(0));
	}
	
	/**
	 * Assign exam date to all students
	 */	
	@PostMapping(value="/assignexamdate")
	public void assignExamDates(HttpServletRequest request) {
		
		Enumeration<String> parameterNames = request.getParameterNames();
		List<StudentModel> studentList = studentService.getRegisteredStudents();
		int nTestableStudents = 0, nRemainingStudents = 0, nExamDays = 0, fivePercentTestable = 0;
		int nRegisteredStudents = studentService.getRegisteredStudents().size();
    	try {
	    	if(parameterNames.hasMoreElements()) {
			
			    Date startDate = (new SimpleDateFormat("MM/dd/yyyy")).parse(String.valueOf(request.getParameter("examStartDate")));
			    Date endDate = (new SimpleDateFormat("MM/dd/yyyy")).parse(String.valueOf(request.getParameter("examEndDate")));
			    Date examDate =  null;
			    nTestableStudents = Integer.valueOf(request.getParameter("nTestableStudents"));
			    nRemainingStudents = Integer.valueOf(request.getParameter("nRemainingStudents"));
			    fivePercentTestable = Integer.valueOf(request.getParameter("fivePercentTestable"));
			    nExamDays = Integer.valueOf(request.getParameter("nExamDays"));
			    Calendar calendar = Calendar.getInstance();
			    
			    /**
			     * x reps the n exam days counter
			     * y reps the n testable students counter
			     * z reps the n registered students counter
			     */
			    int studentIncrement = 0;
			    			    
			    for(int x = 0; x < nExamDays; x++) {
			    	
			    	if(examDate == null) {
			    		examDate = startDate;
			    	}else {
			    		
						calendar.setTime(examDate);
						calendar.add(Calendar.DATE, 1);
						examDate = calendar.getTime();
			    	}
			    	
			    	for(int z = 0; z < nTestableStudents;  z++, studentIncrement++) {
			    		
			    		studentList.get(studentIncrement).setExamDate(examDate);
			    	}			    
			    }
			    
			    if(nRemainingStudents < fivePercentTestable) {
			    	
			    	while(studentIncrement < nRegisteredStudents) {
			    		studentIncrement++;
			    		studentList.get(studentIncrement).setExamDate(endDate);
			    	}
			    	
			    }else if(nRemainingStudents > fivePercentTestable) {
			    	
			    	calendar.setTime(examDate);
					calendar.add(Calendar.DATE, 1);
					examDate = calendar.getTime();
					
			    	while(studentIncrement < nRegisteredStudents) {
			    		studentIncrement++;
			    		studentList.get(studentIncrement).setExamDate(examDate);
			    	}
			    }
		    }
	    }catch(Exception ex) {
    		System.out.println(ex.getMessage()+" error");
		}
		finally {
			for(StudentModel sm : studentList)
				studentService.updateStudent(sm);
		}
	}
	
	@PostMapping(value="/togglesettings")
	public void toggleSettings(HttpServletRequest request) {
		Enumeration<String> parameterNames = request.getParameterNames();
		SettingsModel settingsModel = new SettingsModel();
    	try {
	    	if(parameterNames.hasMoreElements()) {
				settingsModel = settingsService.getSettings(Integer.parseInt(request.getParameter("settingsId")));
				
				switch (String.valueOf(request.getParameter("toggleId"))) {
				
					case "enableStudentReg":
						if(settingsModel.getEnableStudentReg() == 1)
							settingsModel.setEnableStudentReg(0);
						else
							settingsModel.setEnableStudentReg(1);
						break;
					case "enableEmployeeReg":
						if(settingsModel.getEnableEmployeeReg() == 1) 
							settingsModel.setEnableEmployeeReg(0);
						else
							settingsModel.setEnableEmployeeReg(1);
						break;
					case "enableRoleReg":
						if(settingsModel.getEnableRoleReg() == 1) 
							settingsModel.setEnableRoleReg(0);
						else 
							settingsModel.setEnableRoleReg(1);
						break;
					case "enableOnlineExam":
						if(settingsModel.getEnableExam() == 1)
							settingsModel.setEnableExam(0);
						else
							settingsModel.setEnableExam(1);
						break;
					case "enableCourseReg":
						if(settingsModel.getEnableCourseReg() == 1)
							settingsModel.setEnableCourseReg(0);
						else
							settingsModel.setEnableCourseReg(1);
						break;
					case "enableSubjectReg":
						if(settingsModel.getEnableSubjectReg() == 1)
							settingsModel.setEnableSubjectReg(0);
						else
							settingsModel.setEnableSubjectReg(1);
						break;
				}
			}
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage()+" error");
    	}finally {
    		settingsService.Save(settingsModel);
    	}
	}
	
	@GetMapping(value="/settingsapi")
	public String getSettings() {
		return new Gson().toJson(settingsService.getSettings().get(0));
	}
}