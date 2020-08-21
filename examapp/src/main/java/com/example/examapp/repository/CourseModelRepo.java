package com.example.examapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.CourseModel;

@Repository
public interface CourseModelRepo extends JpaRepository <CourseModel, Integer>{
	CourseModel findByCourseName(String course_name);
	CourseModel findById(int courseId);
	
	static final String FIND_SUBJECTS = "SELECT c FROM CourseModel c WHERE c.courseStatus = '1'";
	@Query(value = FIND_SUBJECTS)
	List<CourseModel> findAll();
	
	static final String GET_ACTIVE_COURSES =
			"SELECT courseModel FROM CourseModel courseModel WHERE courseModel.courseStatus = 1";
	@Query(value = GET_ACTIVE_COURSES)
	List<CourseModel> activeCourse();
	
	static final String GET_nACTIVE_COURSES =
			"SELECT COUNT(courseModel) FROM CourseModel courseModel WHERE courseModel.courseStatus = 1";
	@Query(value = GET_nACTIVE_COURSES)
	int nActiveCourse();
}
