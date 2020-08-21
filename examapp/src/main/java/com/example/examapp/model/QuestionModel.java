package com.example.examapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "QuestionModel")
public class QuestionModel {	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "questionId")
	private int questionId;
	
	@Column(name = "questionName")
	@NotEmpty(message = "*Please provide a valid question")
	private String questionName;
	
	@Column(name = "optionA")
	@NotEmpty(message = "*This field is required")
	private String optionA;
	
	@Column(name = "optionB")
	@NotEmpty(message = "*This field is required")
	private String optionB;
	
	@Column(name = "optionC")
	@NotEmpty(message = "*This field is required")
	private String optionC;
	
	@Column(name = "optionD")
	@NotEmpty(message = "*This field is required")
	private String optionD;
	
	@Column(name = "questionAnswer")
	@NotEmpty(message = "*This field is required")
	private String questionAnswer;
	
	@ManyToOne
	@JoinColumn(name = "subjectId", nullable=false)
	//@OnDelete(action=OnDeleteAction.CASCADE)
	private SubjectModel subjectModel;
}
