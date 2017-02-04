package com.kanuhasu.ap.business.bo.complaint;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kanuhasu.ap.business.bo.user.UserEntity;

@Entity
@Table
public class ComplaintEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// instance

	@Id
	@GeneratedValue
	private long id;
	private String title;
	private long complaintNO;
	private String against;
	
	private UserEntity consumer;
	private UserEntity lawyer;
	
	private Date createdOn = new Date();
	private Date lastUpdatedOn = new Date();
	
	//constructor

	// setter-getter

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public UserEntity getConsumer() {
		return consumer;
	}
	public void setConsumer(UserEntity consumer) {
		this.consumer = consumer;
	}
	public long getComplaintNO() {
		return complaintNO;
	}
	public void setComplaintNO(long complaintNO) {
		this.complaintNO = complaintNO;
	}
	public String getAgainst() {
		return against;
	}
	public void setAgainst(String against) {
		this.against = against;
	}
	public UserEntity getLawyer() {
		return lawyer;
	}
	public void setLawyer(UserEntity lawyer) {
		this.lawyer = lawyer;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	// override
}
