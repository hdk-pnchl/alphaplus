package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.bo.user.UserEntity;

@Entity
@Table
public class JobInstEntity implements Serializable {
	private static final long serialVersionUID = -3144173378364863320L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	private String instruction;
	private String title;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private JobEntity job;
	
	private Date createdOn= new Date();
	private Date lastUpdatedOn= new Date();

	private UserEntity lastUpdatedBy;
	
	// setter-getter
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getInstruction() {
		return instruction;
	}
	
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	
	public JobEntity getJob() {
		return job;
	}
	
	public void setJob(JobEntity job) {
		this.job = job;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
}