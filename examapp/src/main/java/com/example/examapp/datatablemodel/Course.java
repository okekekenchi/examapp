package com.example.examapp.datatablemodel;

import java.util.HashSet;
import java.util.Set;

import com.example.examapp.model.SubjectModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

	private Integer courseId;
	private String courseName;	
	private Integer courseStatus;
	private Integer requiredApplicant;
	private Set<SubjectModel> subjectModels = new HashSet<>();

	private String validStatus;
	private String update;
	private String delete;
	
	public Course (Integer courseId, String courseName, Integer courseStatus, Integer requiredApplicant) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.courseStatus = courseStatus;
		this.requiredApplicant = requiredApplicant;
	}
	
	public Course (Integer courseId, String courseName, Integer requiredApplicant) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.requiredApplicant = requiredApplicant;
	}
}