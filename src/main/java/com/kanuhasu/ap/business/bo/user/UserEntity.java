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
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.type.bo.user.Gender;
import com.kanuhasu.ap.business.util.CommonUtil;

@Entity
@Table
public class UserEntity implements Serializable {
	private static final long serialVersionUID = -1457413248383951436L;
	
	// instance
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Date createdOn= new Date();
	private Date lastUpdatedOn= new Date();
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity lastUpdatedBy;
	
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean enabled = true;
	private boolean credentialsNonExpired = true;
	private String changePasswordReqToken = "";
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "roleID"))
	private Set<RoleEntity> roles = new HashSet<RoleEntity>();
	
	private String name;
	private String emailID;
	private String password;
	private Long regNO;
	private Date dob;
	private Gender gender = Gender.MALE;
	private boolean married;
	
	private String education;
	private String occupation;
	
	private String pan;
	private String drivingLicence;
	private String adhar;
	private String passport;
	
	@MapKey(name = "title")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, AddressEntity> addressDetail;
	
	@MapKey(name = "title")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, ContactEntity> contactDetail;
	
	// constructor
	
	public UserEntity() {
		super();
		this.populateRegNo();
	}
	
	/**
	 * This should not be used from anywhere other then constructor
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
	
	public Set<RoleEntity> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
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

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
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

	public UserEntity getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(UserEntity lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	// override
}
