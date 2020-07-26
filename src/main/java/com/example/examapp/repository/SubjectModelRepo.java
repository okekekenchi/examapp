package com.example.examapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.SubjectModel;

@Repository
public interface SubjectModelRepo extends JpaRepository <SubjectModel, Integer>{
	
	SubjectModel findBySubjectName(String subject_name);
	
	SubjectModel findById(int subjectId);
	
	static final String FIND_SUBJECTS = "SELECT s FROM SubjectModel s";
	@Query(value = FIND_SUBJECTS)
	List<SubjectModel> findAll();
	
	static final String DELETE_SUBJECTS =
			"UPDATE SubjectModel s SET s.subjectStatus = 0 WHERE subjectId = :subjectId";
	@Query(value = DELETE_SUBJECTS)
	void deleteSubject(@Param("subjectId") int subjectId);
}
