package com.example.examapp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "StudentModel")
@PrimaryKeyJoinColumn(name="studentId")
public class StudentModel extends UserModel{
	
	@Column(name = "phone")
	@NotEmpty(message = "*Please provide a phone number")
	private String phone;
	
	@Column(name = "address")
	@Length(min = 10, message = "*Your Address must have at least 10 characters")
	@NotEmpty(message = "*Please provide a valid residential address")
	private String address;

	@Column(name = "gender", length=6)
	private String gender;		
	
	@Column(name = "studentScore")
	private  int studentScore;

	@Column(name = "takenTest")
	private  int takenTest;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "examDate")
	@Nullable
	private  Date examDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "courseId", nullable= false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private CourseModel courseModel;
}