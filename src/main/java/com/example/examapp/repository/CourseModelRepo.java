package com.example.examapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.CourseModel;

@Repository
public interface CourseModelRepo extends JpaRepository <CourseModel, Integer>{
	CourseModel findByCourseName(String course_name);
	CourseModel findById(int courseId);
}
