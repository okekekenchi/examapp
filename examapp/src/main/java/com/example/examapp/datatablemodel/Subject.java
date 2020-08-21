package com.example.examapp.datatablemodel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Subject {

	private Integer subjectId;
	private String subjectName;
	private Integer subjectStatus;
	
	private String validStatus;
	private String update;
	private String delete;
	
	public Subject(Integer subjectId, String subjectName, Integer subjectStatus) {
		this.subjectId = subjectId;
		this.subjectName = subjectName;
		this.subjectStatus = subjectStatus;
	}
}