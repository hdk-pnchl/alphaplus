package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.bo.job.LastUpdateEntity;
import com.kanuhasu.ap.business.pojo.Address;
import com.kanuhasu.ap.business.pojo.Contact;
import com.kanuhasu.ap.business.pojo.User;
import com.kanuhasu.ap.business.pojo.UserBasic;
import com.kanuhasu.ap.business.pojo.UserIDDetail;
import com.kanuhasu.ap.business.type.bo.user.Gender;
import com.kanuhasu.ap.business.util.CommonUtil;

@Entity
@Table(name = "User")
public class UserEntity extends LastUpdateEntity implements Serializable {
	private static final long serialVersionUID = -1457413248383951436L;

	/** ------------| instance |------------ **/

	// basic
	private String name;
	private String emailID;
	private String password;
	private Long regNO;
	private Date dob = new Date();
	private Gender gender = Gender.MALE;
	private boolean married;
	private String education;
	private String occupation;
	// id
	private String pan;
	private String drivingLicence;
	private String adhar;
	private String passport;
	// account
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean enabled = true;
	private boolean credentialsNonExpired = true;
	private String changePasswordReqToken = "";
	// roles
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "roleID"))
	private Set<RoleEntity> roles = new HashSet<>();
	// address
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "USER_ADDRESS", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "addressID"))
	private Set<AddressEntity> addresses = new HashSet<>();
	private String addressStr;
	// contact
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "USER_CONTACT", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "contactID"))
	private Set<ContactEntity> contacts = new HashSet<>();
	private String contactStr;

	/** ------------| constructor |------------ **/

	public UserEntity() {
		super();
		this.populateRegNo();
	}

	/** ------------| business |------------ **/

	public User pojo() {
		User pojo = new User();
		// basic
		pojo.setId(this.getId());
		pojo.setName(this.name);
		pojo.setEmailID(this.emailID);
		pojo.setDob(this.dob);
		pojo.setGender(this.gender);
		pojo.setMarried(String.valueOf(this.isMarried()));
		pojo.setEducation(this.education);
		pojo.setOccupation(this.occupation);
		pojo.setRegNO(this.regNO);
		// id
		pojo.setPan(this.pan);
		pojo.setAdhar(this.adhar);
		pojo.setDrivingLicence(this.drivingLicence);
		pojo.setPassport(passport);
		// core
		pojo.setLastUpdatedOn(this.getLastUpdatedOn());
		pojo.setCreatedOn(this.getCreatedOn());
		return pojo;
	}

	public User pojoFull() {
		User pojo = this.pojo();
		// addresses
		Set<Address> addresses = pojo.getAddresses();
		for (AddressEntity entity : this.getAddresses()) {
			addresses.add(entity.pojo());
		}
		// contacts
		Set<Contact> contacts = pojo.getContacts();
		for (ContactEntity entity : this.getContacts()) {
			contacts.add(entity.pojo());
		}
		return pojo;
	}

	public void overrideBasic(UserBasic userBasic) {
		this.setName(userBasic.getName());
		this.setEmailID(userBasic.getEmailID());
		this.setDob(userBasic.getDob());
		this.setGender(userBasic.getGender());
		this.setMarried(userBasic.isMarried());
		this.setEducation(userBasic.getEducation());
		this.setOccupation(userBasic.getOccupation());
	}

	public void overrideID(UserIDDetail idDetail) {
		this.setPan(idDetail.getPan());
		this.setAdhar(idDetail.getAdhar());
		this.setDrivingLicence(idDetail.getDrivingLicence());
		this.setPassport(idDetail.getPassport());
	}

	/**
	 * This should not be used from anywhere other then constructor
	 */
	private void populateRegNo() {
		this.setRegNO(CommonUtil.nextRegNo());
	}

	// setter-getter

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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAddressStr() {
		return addressStr;
	}

	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
	}

	public String getContactStr() {
		return contactStr;
	}

	public void setContactStr(String contactStr) {
		this.contactStr = contactStr;
	}

	public Set<AddressEntity> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<AddressEntity> addresses) {
		this.addresses = addresses;
	}

	public Set<ContactEntity> getContacts() {
		return contacts;
	}

	public void setContacts(Set<ContactEntity> contacts) {
		this.contacts = contacts;
	}

	// override
}
