package com.nanites.alphaplus.business.bo.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nanites.alphaplus.business.type.bo.user.Gender;
import com.nanites.alphaplus.business.util.CommonUtil;

@Entity
@Table
public class BasicDetailEntity implements Serializable {
	private static final long serialVersionUID = -1040263764731799394L;
	
	// Instance
	
	@Id
	@GeneratedValue
	private long id;
	
	private String name;
	private String emailID;
	private String password;
	private long regNO;
	private long contactNO;
	
	private int age;
	private Gender gender= Gender.MALE;
	private boolean married;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "user")
	private UserEntity user;
	
	public BasicDetailEntity() {
		super();
		this.populateRegNo();
	}

	/**
	 * This should not be used from anywhere other then BasicDetailEntity
	 * constructor
	 */
	private void populateRegNo() {
		this.setRegNO(CommonUtil.nextRegNo());
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
	
	public long getRegNO() {
		return regNO;
	}
	
	public void setRegNO(long regNO) {
		this.regNO = regNO;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public long getContactNO() {
		return contactNO;
	}
	
	public void setContactNO(long contactNO) {
		this.contactNO = contactNO;
	}	
	
	public UserEntity getUser() {
		return user;
	}
	
	public void setUser(UserEntity user) {
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
}
