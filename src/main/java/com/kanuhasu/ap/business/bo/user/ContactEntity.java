package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class ContactEntity implements Serializable {
	private static final long serialVersionUID = -2341973230214246584L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	private String title;
	private String no;
	private String contactStr;
	
	private Date createdOn= new Date();
	private Date lastUpdatedOn= new Date();
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity lastUpdatedBy;
	
	//constructor
	
	// setter-getter
	
	public String getNo() {
		return no;
	}
	
	public void setNo(String no) {
		this.no = no;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getContactStr() {
		return contactStr;
	}
	
	public void setContactStr(String contactStr) {
		this.contactStr = contactStr;
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// override
	@Override
	public String toString() {
		StringBuilder strBuilding= new StringBuilder();
		strBuilding
		.append("[ ")
		.append(this.title).append(" : ")
		.append(this.no).append(" ]");
		
		return strBuilding.toString();
	}	
}