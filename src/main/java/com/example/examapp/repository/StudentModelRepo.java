package com.example.examapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.StudentModel;

@Repository
public interface StudentModelRepo extends JpaRepository<StudentModel, Integer>{
	StudentModel findByEmail(String email);
	
	StudentModel findById(int studentId);
	
	static final String GET_REGISTERED_STUDENTS_BY_YEAR =
			"SELECT studentModel FROM StudentModel studentModel WHERE YEAR(studentModel.regdate) = :year";
	@Query(value = GET_REGISTERED_STUDENTS_BY_YEAR)
	List<StudentModel> findRegisteredStudents(@Param("year") int year);
	
	static final String GET_ACTIVE_STUDENTS_BY_YEAR =
			"SELECT COUNT(studentModel) FROM StudentModel studentModel WHERE YEAR(studentModel.regdate) = :year"+
			" AND studentModel.status = 1";
	@Query(value = GET_ACTIVE_STUDENTS_BY_YEAR)
	int nActiveStudents(@Param("year") int year);
	
	static final String GET_ONLINE_STUDENTS_BY_YEAR =
			"SELECT COUNT(studentModel) FROM StudentModel studentModel WHERE YEAR(studentModel.regdate) = :year"+
			" AND studentModel.online = 1";
	@Query(value = GET_ONLINE_STUDENTS_BY_YEAR)
	int nStudentsOnline(@Param("year") int year);
}
