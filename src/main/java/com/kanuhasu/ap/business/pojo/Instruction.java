package com.kanuhasu.ap.business.pojo;

import java.util.Date;

import com.kanuhasu.ap.business.bo.job.InstructionEntity;

public class Instruction {
	/** ------------| instance |------------ **/

	private long id;
	private String value;
	private String title;
	private Date createdOn = new Date();
	private Date lastUpdatedOn = new Date();
	private long jobID;
	private String part;

	/** ------------| business |------------ **/

	public InstructionEntity entity() {
		InstructionEntity entity = new InstructionEntity();
		entity.setId(id);
		entity.setTitle(title);
		entity.setValue(value);
		return entity;
	}

	/** ------------| setter-getter |------------ **/

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public long getJobID() {
		return jobID;
	}

	public void setJobID(long jobID) {
		this.jobID = jobID;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}
}