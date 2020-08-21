package com.example.examapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardModel {

	private int nstudent;
	private int nsubject;
	private int ncourse;
	private int nstudentOnline;
	private int nvendor;
	private int nvendorOnline;
	private int nquestion;
	private int nemployee;
	private int nemployeeOnline;
	private int ncomputer;
	private int nvoucher;
	private String username;
	private String profile;
	private int nvoucherUsed;
	private int nrole;	
}
