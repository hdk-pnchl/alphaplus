package com.kanuhasu.ap.business.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.kanuhasu.ap.business.bo.job.ChallanEntity;
import com.kanuhasu.ap.business.bo.job.Status;

public class Challan {
	private Long exeByID;
	private Status status;
	private Date date;
	private Long id;

	private User exeBy;
	private Set<Instruction> instructions = new HashSet<>();

	private long jobID;
	private Date lastUpdatedOn = new Date();

	/** ------------| business |------------ **/

	public ChallanEntity entity() {
		ChallanEntity entity = new ChallanEntity();
		entity.setDate(date);
		entity.setExeBy(exeBy.entity());
		entity.setStatus(status);
		entity.setId(id);
		return entity;
	}

	/** ------------| setter-getter |------------ **/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExeByID() {
		return exeByID;
	}

	public void setExeByID(Long exeByID) {
		this.exeByID = exeByID;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Set<Instruction> getInstructions() {
		return instructions;
	}

	public void setInstructions(Set<Instruction> instructions) {
		this.instructions = instructions;
	}

	public User getExeBy() {
		return exeBy;
	}

	public void setExeBy(User exeBy) {
		this.exeBy = exeBy;
	}

	public long getJobID() {
		return jobID;
	}

	public void setJobID(long jobID) {
		this.jobID = jobID;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
}