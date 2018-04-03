package com.kanuhasu.ap.business.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.kanuhasu.ap.business.bo.job.BillEntity;
import com.kanuhasu.ap.business.bo.job.Status;

public class Bill {
	private Long exeByID;
	private Status status;
	private Date date = new Date();
	private Long id;

	private User exeBy;
	private Set<Instruction> instructions = new HashSet<>();

	private long jobID;
	private Date lastUpdatedOn = new Date();

	/** ------------| business |------------ **/

	public BillEntity entity() {
		BillEntity entity = new BillEntity();
		entity.setDate(date);
		entity.setExeBy(exeBy.entity());
		entity.setStatus(status);
		entity.setId(id);
		// pojo.setInstructions(instructions);

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

	public User getExeBy() {
		return exeBy;
	}

	public void setExeBy(User exeBy) {
		this.exeBy = exeBy;
	}

	public Set<Instruction> getInstructions() {
		return instructions;
	}

	public void setInstructions(Set<Instruction> instructions) {
		this.instructions = instructions;
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