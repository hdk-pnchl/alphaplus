package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;

@Entity
@Table
public class ClientEntity implements Serializable {
	private static final long serialVersionUID = 5696903605244349608L;
	
	// instance
	
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String emailID;
	
	@MapKey(name = "title")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, AddressEntity> addressDetail;
	
	@MapKey(name = "title")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, ContactEntity> contactDetail;
	
	private Date createdOn= new Date();
	private Date lastUpdatedOn= new Date();

	private UserEntity lastUpdatedBy;
	
	// setter-getter
	
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
	
	public String getEmailID() {
		return emailID;
	}
	
	public void setEmailID(String emailID) {
		this.emailID = emailID;
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

	// constructor
	
	// override
}