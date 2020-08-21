package com.example.examapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.datatablemodel.Question;
import com.example.examapp.model.QuestionModel;
import com.example.examapp.model.QuestionProp;
import com.example.examapp.model.StudentModel;
import com.example.examapp.model.SubjectModel;
import com.example.examapp.pagination.DataTableResults;
import com.example.examapp.service.FileSystemStorageService;
import com.example.examapp.service.QuestionService;
import com.example.examapp.service.StudentService;
import com.example.examapp.service.SubjectService;
import com.google.gson.Gson;

@RestController
public class QuestionController {
	
	  @Autowired QuestionService questionService;
	  @Autowired SubjectService subjectService;
	  @Autowired StudentService studentService;
	  @Autowired FileSystemStorageService storageService;
	  @PersistenceContext private EntityManager entityManager;
	  
	  @GetMapping(value = "/question")
	  public ModelAndView getQuestion(ModelAndView mv) {
		  mv.addObject("questionModel", new QuestionModel());
		  mv.addObject("subjectList", subjectService.getSubjects());
		  return mv; 
	  }
	  
	//This function adds and makes updates
	@PostMapping("/question")
	public ModelAndView addQuestion(@Valid QuestionModel questionModel, BindingResult bindingResult,
			ModelAndView mv ) {

		if (!bindingResult.hasErrors()) {
			questionService.saveQuestion(questionModel);
			mv.addObject("questionModel", new QuestionModel());
			mv.addObject("subjectList", subjectService.getSubjects());
			mv.addObject("Message", "Question has been added successfully");
		}
		return mv;
	}
  
	@DeleteMapping("/question")
	public void deleteQuestion(HttpServletRequest request, HttpServletResponse response, QuestionModel questionModel) {
		
		Enumeration<String> parameterNames = request.getParameterNames();
    	
    	if(parameterNames.hasMoreElements()) {
			questionService.deletebyId(Integer.parseInt(request.getParameter("questionId")));
    	}
	}
		
	@PostMapping("/questions/fetch")
	public String fetchQuestion(HttpServletRequest request, HttpServletResponse response, QuestionModel questionModel) {
		Enumeration<String> parameterNames = request.getParameterNames();
    	
    	if(parameterNames.hasMoreElements()) {    			
    		questionModel = questionService.findById(Integer.parseInt(request.getParameter("questionId")));
		}
    	
    	Question question = new Question(
    			questionModel.getQuestionId(), questionModel.getQuestionName(),
    			questionModel.getOptionA(), questionModel.getOptionB(),
    			questionModel.getOptionC(), questionModel.getOptionD(),
    			questionModel.getQuestionAnswer(),
    			questionModel.getSubjectModel().getSubjectName(),
    			questionModel.getSubjectModel().getSubjectId()
			);
    	
    	return new Gson().toJson(question);
	}
	
	@SuppressWarnings("unchecked")
  	@GetMapping("/questions/paginated/")
	public String listQuestionsPaginated(HttpServletRequest request, HttpServletResponse response) {
		
		List<QuestionModel> questionModelList = new ArrayList<>();
		List<Question> questionList = new ArrayList<>();
		DataTableResults<Question> dataTableResult = new DataTableResults<Question>();
		Enumeration<String> parameterNames = request.getParameterNames();
		
    	if(parameterNames.hasMoreElements()) {
    		Query query = entityManager.createNativeQuery(
    				questionService.dataTableQuery(request), QuestionModel.class);
    		questionModelList = query.getResultList();

    		Query query1 = entityManager.createNativeQuery("SELECT * FROM QuestionModel");
    		    		
    		for(int i=0; i<questionModelList.size(); i++) {
    			questionList.add(
					new Question(
							questionModelList.get(i).getQuestionId(),
							questionModelList.get(i).getQuestionName(),
							questionModelList.get(i).getOptionA(),
							questionModelList.get(i).getOptionB(),
							questionModelList.get(i).getOptionC(),
							questionModelList.get(i).getOptionD(),
							questionModelList.get(i).getQuestionAnswer(),
							questionModelList.get(i).getSubjectModel().getSubjectName(),
							questionModelList.get(i).getSubjectModel().getSubjectId()
						)
					);
    		}
    		
    		for(Question question : questionList) {
    			
    			question.setUpdate("<img src='/images/edit.png' style='width:25px; height:25px;' name='update' class='update' id='"+question.getQuestionId()+"' alt='Delete'>");
    			question.setDelete("<img src='/images/delete.png' style='width:25px; height:25px;' name='delete' class='delete' id='"+question.getQuestionId()+"' alt='Delete'>");
    		}

    		dataTableResult.setDraw(request.getParameter("draw").toString());
    		dataTableResult.setRecordsFiltered(Integer.toString(query1.getResultList().size()));
    		dataTableResult.setRecordsTotal(Integer.toString(questionList.size()));
    		dataTableResult.setListOfDataObjects(questionList);	
		}
		return new Gson().toJson(dataTableResult);
	}
	
	@PostMapping("/uploadquestion")
	public ModelAndView uploadQuestion(ModelAndView mv, @RequestParam(value="file") MultipartFile questions) {
		
		mv.addObject("questionModel", new QuestionModel());
		mv.addObject("subjectList", subjectService.getSubjects());
		mv.setViewName("/question");
		
		storageService.initUpload();
		storageService.Store(questions);
		
		Workbook workbook = null;
		try {
			/**
			 * Open file from a given location
			 */
			File file = new File("upload/" + StringUtils.cleanPath(questions.getOriginalFilename()));
			FileInputStream fileInputStream = new FileInputStream(file);
			workbook = new XSSFWorkbook(fileInputStream);

			/**
			 * Retrieves the first sheet of the file and iterate through each row
			 * Sheet should not exceed 1
			 * rows should not exceed 100
			 */
			Sheet sheet = workbook.getSheetAt(0);
			
			if(sheet.getPhysicalNumberOfRows() > 100)
				return mv;
			
			for(Row row : sheet) {					
				Double subjectId = row.getCell(6).getNumericCellValue();
				
				questionService.saveQuestion(
						
					new QuestionModel(
						0,
						assignValueBasedOnDatatype(row.getCell(0)),
						assignValueBasedOnDatatype(row.getCell(1)),
						assignValueBasedOnDatatype(row.getCell(2)),
						assignValueBasedOnDatatype(row.getCell(3)),
						assignValueBasedOnDatatype(row.getCell(4)),
						assignValueBasedOnDatatype(row.getCell(5)),
						subjectService.findById(Integer.valueOf(subjectId.intValue()))
					)
				);
				
			}	
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		finally {
			try {
				if(workbook != null)
					workbook.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			storageService.Delete(questions);
			
		}
		return mv; 
	}
	
	@SuppressWarnings("deprecation")
	private String assignValueBasedOnDatatype(Cell cell) {
		String returnValue = "";
		switch(cell.getCellTypeEnum()) {
			case BLANK:
			break;
			case BOOLEAN:
				returnValue = String.valueOf(cell.getBooleanCellValue());
			break;
			case ERROR:
			break;
			case FORMULA:
			break;
			case NUMERIC:
				returnValue = String.valueOf(cell.getNumericCellValue());
			break;
			case STRING:
				returnValue = String.valueOf(cell.getRichStringCellValue());
			break;
			case _NONE:
			break;
		}
			return returnValue;		
	}
	

	@GetMapping("/questionproperty/{studentId}")
	public List<QuestionProp> getQuestionProperty(@PathVariable int studentId){
		List<QuestionProp> questionProperty = new ArrayList<>();
		List<SubjectModel> subjectList = new ArrayList<>();
		
		StudentModel studentModel = studentService.findById(studentId);
		subjectList = subjectService.subjectRegistered(studentModel.getCourseModel().getCourseId());
		
		for(int i=0; i<subjectList.size(); i++) 
			questionProperty.add(new QuestionProp(
						subjectList.get(i).getSubjectId(),
						subjectList.get(i).getSubjectName())
					);
		
		return questionProperty;
	}
}