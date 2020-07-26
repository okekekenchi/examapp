package com.example.examapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionProp {
	
	private int subjectId;
	private String subjectName;
	private int totalScore = 0;
	private int currentQuestionNumber = 0;
	private int totalNumberOfQuestions = 0;
	
	public QuestionProp(int subjectId, String subjectName, int totalNumberOfQuestions) {
		this.subjectId = subjectId;
		this.subjectName = subjectName;
		this.totalNumberOfQuestions =totalNumberOfQuestions;
	}
	
	public QuestionProp(int subjectId, String subjectName) {
		this.subjectId = subjectId;
		this.subjectName = subjectName;
	}
}
