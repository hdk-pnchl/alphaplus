package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private long id;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
	private BasicDetailEntity basicDetail = new BasicDetailEntity();
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
	private AddressEntity address = new AddressEntity();
	
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
	
	//constructor
	
	public UserEntity() {
	}
	
	// setter-getter
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
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
	
	public AddressEntity getAddress() {
		return address;
	}
	
	public void setAddress(AddressEntity address) {
		this.address = address;
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
	
	public BasicDetailEntity getBasicDetail() {
		return basicDetail;
	}
	
	public void setBasicDetail(BasicDetailEntity basicDetail) {
		this.basicDetail = basicDetail;
	}
	
	public String getChangePasswordReqToken() {
		return changePasswordReqToken;
	}
	
	public void setChangePasswordReqToken(String changePasswordReqToken) {
		this.changePasswordReqToken = changePasswordReqToken;
	}
	
	// override
}
