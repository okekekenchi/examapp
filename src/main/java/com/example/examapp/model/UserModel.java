package com.example.examapp.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UserModel")
@Inheritance(strategy = InheritanceType.JOINED)

public class UserModel {
	
	public UserModel(UserModel user) {
		this.email = user.email;
		this.password = user.password;
		this.roleName = user.roleName;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userId")
	private Integer userId;
	
	@Column(name = "firstName", length=25)
	@Length(min = 2, max = 25, message = "*Your first name must have at least 2 and a maximum of 25 characters")
	@NotEmpty(message = "*Please provide your first name")
	private String firstName;
	
	@Column(name = "lastName", length=25)
	@Length(min = 2, max = 25, message = "*Your last name must have at least 2 and a maximum of 25 characters")
	@NotEmpty(message = "*Please provide your last name")
	private String lastName;
	
	@Column(name = "otherName", nullable=true, length=25)
	private String otherName;
	
	@Column(name = "email", length=150)
	@Email(message = "*Please provide a valid Email")
	@NotEmpty(message = "*Please provide an email")
	private String email;
	
	@Column(name = "password", length=255)
	@Length(min = 6, message = "*Your password must have at least 6 characters")
	@NotEmpty(message = "*Please provide your password")
	@Transient
	private String password;	

	@Column(name = "status", length=1)
	private int status;
	
	@Column(name = "online", length=1)
	private int online;
	
	@Lob
	@Column(name = "image")
	private byte[] image;	
		
	@Temporal(TemporalType.DATE)
	@Column(name = "regDate", nullable=false)
	private Date regdate = new java.sql.Date(System.currentTimeMillis());
	
	@ManyToMany(cascade = CascadeType.ALL,  fetch = FetchType.EAGER)
	@JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "userId",
	referencedColumnName = "userId"),
	inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "roleId"))
	private Set<RoleModel> roleName;
}
