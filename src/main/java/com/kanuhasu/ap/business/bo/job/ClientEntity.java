package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "CLIENT_ADDRESS", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "addressID"))
	private Set<AddressEntity> addresses;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "CLIENT_CONTACT", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "contactID"))
	private Set<ContactEntity> contacts;

	private Date createdOn = new Date();
	private Date lastUpdatedOn = new Date();

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
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

	// constructor

	// override
}