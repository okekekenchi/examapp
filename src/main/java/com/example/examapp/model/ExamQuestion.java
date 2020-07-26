package com.example.examapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestion {
	
	private int  questionId;
    private String questionName;
    private int questionNumber;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;
    private String questionSubject;
    private String optionSelected;
    private boolean selected;
}
