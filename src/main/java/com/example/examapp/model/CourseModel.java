package com.example.examapp.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "CourseModel")

public class CourseModel {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="courseId")
	private Integer courseId;
	
	@Column(name="courseName", length=50)
	@NotEmpty(message = "*Please provide a valid course")
	private String courseName;
	
	@Column(name = "courseStatus", length=1)
	private Integer courseStatus;
	
	@Column(name = "requiredApplicant")
	private Integer requiredApplicant;
	
	@OneToMany(mappedBy="courseModel", fetch = FetchType.EAGER)
	private Set<StudentModel> studentModels = new HashSet<>();
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinTable(name = "CourseSubject", joinColumns = @JoinColumn(name = "courseId",
	referencedColumnName = "courseId"),
	inverseJoinColumns = @JoinColumn(name = "subjectId", referencedColumnName = "subjectId"))
	private Set<SubjectModel> subjectModels = new HashSet<>();	
}