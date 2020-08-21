package com.example.examapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.examapp.model.QuestionModel;

@Repository
public interface QuestionModelRepo extends JpaRepository <QuestionModel, Integer>{
	QuestionModel findByQuestionName(String question_name);
	QuestionModel findById(int questionId);
}
