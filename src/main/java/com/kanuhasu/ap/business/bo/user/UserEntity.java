package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.type.bo.user.Gender;
import com.kanuhasu.ap.business.util.CommonUtil;

@Entity
@Table
public class UserEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1457413248383951436L;
	
	// instance
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Date createdOn = new Date();
	private Date lastUpdatedOn = new Date();
	
	private boolean isAccountExpired = true;
	private boolean isAccountLocked = true;
	private boolean isAccountEnabled = true;
	private boolean isAccountCredentialsExpired = true;
	private String changePasswordReqToken = "";
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "roleID"))
	private Set<RoleEntity> roles = new HashSet<RoleEntity>();
	
	private String name;
	private String emailID;
	private String password;
	private Long regNO;
	private int age;
	private Gender gender = Gender.MALE;
	private boolean married;
	
	private String education;
	private String occupation;
	
	@MapKey(name = "name")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, AddressEntity> addressDetail;
	
	@MapKey(name = "name")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, ContactEntity> contactDetail;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
	private IDDetailEntity idDetail = new IDDetailEntity();
	
	// constructor
	
	public UserEntity() {
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
	
	// setter-getter
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public Set<RoleEntity> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}
	
	public boolean isAccountExpired() {
		return isAccountExpired;
	}
	
	public void setAccountExpired(boolean isAccountExpired) {
		this.isAccountExpired = isAccountExpired;
	}
	
	public boolean isAccountLocked() {
		return isAccountLocked;
	}
	
	public void setAccountLocked(boolean isAccountLocked) {
		this.isAccountLocked = isAccountLocked;
	}
	
	public boolean isAccountEnabled() {
		return isAccountEnabled;
	}
	
	public void setAccountEnabled(boolean isAccountEnabled) {
		this.isAccountEnabled = isAccountEnabled;
	}
	
	public boolean isAccountCredentialsExpired() {
		return isAccountCredentialsExpired;
	}
	
	public void setAccountCredentialsExpired(boolean isAccountCredentialsExpired) {
		this.isAccountCredentialsExpired = isAccountCredentialsExpired;
	}
	
	public String getChangePasswordReqToken() {
		return changePasswordReqToken;
	}
	
	public void setChangePasswordReqToken(String changePasswordReqToken) {
		this.changePasswordReqToken = changePasswordReqToken;
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
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
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
	
	public Map<String, ContactEntity> getContactDetail() {
		return contactDetail;
	}
	
	public void setContactDetail(Map<String, ContactEntity> contactDetail) {
		this.contactDetail = contactDetail;
	}
	
	public Map<String, AddressEntity> getAddressDetail() {
		return addressDetail;
	}
	
	public void setAddressDetail(Map<String, AddressEntity> addressDetail) {
		this.addressDetail = addressDetail;
	}
	
	public IDDetailEntity getIdDetail() {
		return idDetail;
	}
	
	public void setIdDetail(IDDetailEntity idDetail) {
		this.idDetail = idDetail;
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
	
	// override
}
