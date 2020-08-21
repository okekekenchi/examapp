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
import com.example.examapp.datatablemodel.VoucherSales;
import com.example.examapp.model.CourseModel;
import com.example.examapp.model.StudentModel;
import com.example.examapp.pagination.DataTableResults;
import com.example.examapp.service.CourseService;
import com.example.examapp.service.FileSystemStorageService;
import com.example.examapp.service.VoucherService;
import com.google.gson.Gson;

@RestController
public class VoucherSalesController {

	@Autowired
	private VoucherService voucherService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@GetMapping("/vouchersales")
	public ModelAndView getAdmissionList(ModelAndView mv) {
		mv.addObject("studentModel", new StudentModel());
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/vouchersales/paginated")
	public String listCoursesPaginated(HttpServletRequest request, HttpServletResponse response) {
		
		List<VoucherSales> voucherSales = new ArrayList<>();
		DataTableResults<VoucherSales> dataTableResult = new DataTableResults<>();
		Enumeration<String> parameterNames = request.getParameterNames();
		
    	if(parameterNames.hasMoreElements()) {    		
    		Query query = entityManager.createNativeQuery(
    				voucherService.dataTableQuery(request));
    		voucherSales = query.getResultList();

    		Query recordCount = entityManager.createNativeQuery("SELECT * FROM CourseModel");
  
    		for(int i=0; i<voucherSales.size() ; i++) {
    			voucherSales.add(
					new VoucherSales(
							voucherSales.get(i).getUserId(),
							voucherSales.get(i).getFirstName(),
							voucherSales.get(i).getLastName(),
							voucherSales.get(i).getOtherName(),
							voucherSales.get(i).getSales()
						)
					);
    		}
    		
    		dataTableResult.setDraw(request.getParameter("draw").toString());
    		dataTableResult.setRecordsFiltered(Integer.toString(recordCount.getResultList().size()));
    		dataTableResult.setRecordsTotal(Integer.toString(voucherSales.size()));
    		dataTableResult.setListOfDataObjects(voucherSales);		
		}
		return new Gson().toJson(dataTableResult);
	}
	
//	@PostMapping("/admissionlist/export/{courseId}/{year}")
//	public void exportAdmissionList(@PathVariable int courseId, @PathVariable String year) {
//				
//		String queryString = "SELECT * "+
//							"FROM usermodel as u "+
//							"INNER JOIN studentmodel s ON u.userId = s.studentId "+
//							"INNER JOIN coursemodel c ON s.courseId = c.courseId "+
//							"WHERE c.courseId = " + courseId +
//							" AND u.regDate like '%" + year + "%'" +
//							" ORDER BY s.studentscore DESC LIMIT "+ courseService.findById(courseId).getRequiredApplicant();
//		
//	}
}