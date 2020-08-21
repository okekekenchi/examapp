package com.example.examapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.SubjectModel;

@Repository
public interface SubjectModelRepo extends JpaRepository <SubjectModel, Integer>{
	
	static final String FIND_BY_SUBJECT =
			"SELECT s FROM SubjectModel s WHERE s.subjectName = :subjectName AND s.subjectStatus = '1'";
	@Query(value = FIND_BY_SUBJECT)
	SubjectModel findBySubjectName(@Param("subjectName") String subjectName);
	
	static final String FIND_BY_ID =
			"SELECT s FROM SubjectModel s WHERE s.subjectId = :subjectId AND s.subjectStatus = '1'";
	@Query(value = FIND_BY_ID)
	SubjectModel findById(@Param("subjectId") int subjectId);
	
	static final String FIND_SUBJECTS = "SELECT s FROM SubjectModel s WHERE s.subjectStatus = '1'";
	@Query(value = FIND_SUBJECTS)
	List<SubjectModel> findAll();
	
	static final String DELETE_SUBJECTS =
			"UPDATE SubjectModel subjectModel SET subjectModel.subjectStatus = 0 WHERE subjectModel.subjectId = :subjectId";
	@Query(value = DELETE_SUBJECTS)
	void deleteSubject(@Param("subjectId") int subjectId);
	
	static final String GET_nACTIVE_SUBJECTS =
			"SELECT COUNT(subjectModel) FROM SubjectModel subjectModel WHERE subjectModel.subjectStatus = 1";
	@Query(value = GET_nACTIVE_SUBJECTS)
	int nActiveSubjects();
}
