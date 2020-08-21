package com.example.examapp.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SubjectModel")
public class SubjectModel {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="subjectId")
	private Integer subjectId;
	
	@Column(name="subjectName", length=50)
	private String subjectName;
	
	@Column(name = "subjectStatus", nullable=false, length=1)
	private Integer subjectStatus;
	
	@OneToMany(mappedBy="subjectModel")
	private Set<QuestionModel> questionModels = new HashSet<>();
}
