package com.kanuhasu.ap.business.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.type.bo.user.Gender;

public class User {
	/** ------------| instance |------------ **/

	// basic
	private Long id;
	private String name;
	private String emailID;
	private Long regNO;
	private Date dob = new Date();
	private Gender gender = Gender.MALE;
	private String married;
	private String education;
	private String occupation;
	// id
	private String pan;
	private String drivingLicence;
	private String adhar;
	private String passport;
	// address
	private Set<Address> addresses = new HashSet<>();
	// contact
	private Set<Contact> contacts = new HashSet<>();
	// roles
	private Set<Role> roles = new HashSet<>();
	// core
	private Date createdOn;
	private Date lastUpdatedOn;

	/** ------------| business |------------ **/

	public UserEntity entity() {
		UserEntity entity = new UserEntity();
		entity.setName(name);
		entity.setEmailID(emailID);
		entity.setRegNO(regNO);
		entity.setDob(dob);
		entity.setGender(gender);
		entity.setMarried(Boolean.valueOf(married));
		entity.setEducation(education);
		entity.setOccupation(occupation);
		entity.setPan(pan);
		entity.setDrivingLicence(drivingLicence);
		entity.setAdhar(adhar);
		entity.setPassport(passport);
		return entity;
	}

	/** ------------| setter-getter |------------ **/

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

	public String getMarried() {
		return married;
	}

	public void setMarried(String married) {
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

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getDrivingLicence() {
		return drivingLicence;
	}

	public void setDrivingLicence(String drivingLicence) {
		this.drivingLicence = drivingLicence;
	}

	public String getAdhar() {
		return adhar;
	}

	public void setAdhar(String adhar) {
		this.adhar = adhar;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
}