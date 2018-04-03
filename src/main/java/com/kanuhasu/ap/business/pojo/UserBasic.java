package com.kanuhasu.ap.business.pojo;

import java.util.Date;

import com.kanuhasu.ap.business.type.bo.user.Gender;

public class UserBasic {
	private Long id;
	private String name;
	private String emailID;
	private String password;
	private Long regNO;
	private Date dob = new Date();
	private Gender gender = Gender.MALE;
	private boolean married;
	private String education;
	private String occupation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getRegNO() {
		return regNO;
	}

	public void setRegNO(Long regNO) {
		this.regNO = regNO;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
}
