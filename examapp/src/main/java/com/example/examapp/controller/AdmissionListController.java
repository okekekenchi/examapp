package com.example.examapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.examapp.datatablemodel.AdmissionList;
import com.example.examapp.model.CourseModel;
import com.example.examapp.model.StudentModel;
import com.example.examapp.pagination.DataTableResults;
import com.example.examapp.service.CourseService;
import com.example.examapp.service.FileSystemStorageService;
import com.google.gson.Gson;

@RestController
public class AdmissionListController {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private FileSystemStorageService fileSystemStorageService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@GetMapping("/admissionlist")
	public ModelAndView getAdmissionList(ModelAndView mv) {
		mv.addObject("studentModel", new StudentModel());
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/admissionlist/paginated")
	public String listCoursesPaginated(HttpServletRequest request, HttpServletResponse response) {
		
		List<CourseModel> courseModelList = new ArrayList<>();
		List<AdmissionList> admissionList = new ArrayList<>();
		DataTableResults<AdmissionList> dataTableResult = new DataTableResults<>();
		Enumeration<String> parameterNames = request.getParameterNames();
		
    	if(parameterNames.hasMoreElements()) {    		
    		Query query = entityManager.createNativeQuery(
    				courseService.dataTableQuery(request), CourseModel.class);
    		courseModelList = query.getResultList();

    		Query recordCount = entityManager.createNativeQuery("SELECT * FROM CourseModel");
  
    		for(int i=0; i<courseModelList.size() ; i++) {
    			admissionList.add(
					new AdmissionList(
							courseModelList.get(i).getCourseId(),
							courseModelList.get(i).getCourseName(),
							"<img src='/images/export.png' style='width:30px; height:28px;' name='export' class='export' id='"+courseModelList.get(i).getCourseId()+"' alt='Export'/>"
						)
					);
    		}
    		
    		dataTableResult.setDraw(request.getParameter("draw").toString());
    		dataTableResult.setRecordsFiltered(Integer.toString(recordCount.getResultList().size()));
    		dataTableResult.setRecordsTotal(Integer.toString(admissionList.size()));
    		dataTableResult.setListOfDataObjects(admissionList);		
		}
		return new Gson().toJson(dataTableResult);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/admissionlist/export/{courseId}/{year}")
	public void exportAdmissionList(@PathVariable int courseId, @PathVariable String year) {
				
		fileSystemStorageService.initExport();
		
		List<StudentModel> studentModelList = new ArrayList<>();
		String queryString = "SELECT * "+
							"FROM usermodel as u "+
							"INNER JOIN studentmodel s ON u.userId = s.studentId "+
							"INNER JOIN coursemodel c ON s.courseId = c.courseId "+
							"WHERE c.courseId = " + courseId +
							" AND u.regDate like '%" + year + "%'" +
							" ORDER BY s.studentscore DESC LIMIT "+ courseService.findById(courseId).getRequiredApplicant();
		try {
			Query query = entityManager.createNativeQuery(queryString, StudentModel.class);
			studentModelList = query.getResultList();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		Workbook workbook = new XSSFWorkbook();
		try {
			Sheet sheet = workbook.createSheet("sheetname");
			sheet.setColumnWidth(0, 2000);
			sheet.setColumnWidth(1, 12000);
			sheet.setColumnWidth(2, 4000);
			
			Row courseHeader = sheet.createRow(0);
			Row header = sheet.createRow(1);
			
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.OLIVE_GREEN.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			XSSFFont font = ((XSSFWorkbook)workbook).createFont();
			font.setFontName("Arial");
			font.setFontHeightInPoints((short) 16);
			font.setBold(true);			
			headerStyle.setFont(font);
			
			Cell courseHeaderCell = courseHeader.createCell(0, CellType.STRING);
			courseHeaderCell.setCellValue(courseService.findById(courseId).getCourseName().toUpperCase());
			
			Cell headerCell = header.createCell(0);
			headerCell.setCellValue("S/N");
			headerCell.setCellStyle(headerStyle);
			
			headerCell = header.createCell(2);
			headerCell.setCellValue("ID");
			headerCell.setCellStyle(headerStyle);
			
			headerCell = header.createCell(1);
			headerCell.setCellValue("NAME");
			headerCell.setCellStyle(headerStyle);
			
			CellStyle style = workbook.createCellStyle();
			style.setWrapText(true);
			
			for(int rowNum = 2,serialNum = 1, counter = 0; counter < (studentModelList.size()); rowNum++, serialNum++, counter++) {
				Row row = sheet.createRow(rowNum);
				
				Cell cell = row.createCell(0, CellType.NUMERIC);
				cell.setCellValue(serialNum);
				cell.setCellStyle(style);
				
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(studentModelList.get(counter).getFirstName()+" "+ studentModelList.get(counter).getLastName() + " "+ studentModelList.get(counter).getOtherName());
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(studentModelList.get(counter).getUserId());
				cell.setCellStyle(style);
			}
		
			File file = new File("export/" + StringUtils.cleanPath(courseService.findById(courseId).getCourseName()+".xlsx"));
			FileOutputStream outputStream = new FileOutputStream(file);
			workbook.write(outputStream);
			if(workbook != null)
				workbook.close();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	
		
	}
}
