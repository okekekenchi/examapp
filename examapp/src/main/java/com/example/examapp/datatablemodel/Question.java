package com.example.examapp.datatablemodel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Question {
	
	private Integer questionId;	
	private String questionName;
	private String optionA;
	private String optionB;
	private String optionC;
	private String optionD;
	private String questionAnswer;
	private String subjectName;
	private int subjectId;
	
	private String update;
	private String delete;
	
	public Question(Integer questionId, String questionName, String optionA, String optionB, String optionC,
			String optionD, String questionAnswer, String subjectName, int subjectId) {
		
		this.questionId = questionId;
		this.questionName = questionName;
		this.optionA = optionA;
		this.optionB = optionB;
		this.optionC = optionC;
		this.optionD = optionD;
		this.questionAnswer = questionAnswer;
		this.subjectName = subjectName;
		this.subjectId = subjectId;
	}
}
