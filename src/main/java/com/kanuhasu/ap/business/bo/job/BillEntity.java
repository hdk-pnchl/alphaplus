package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.bo.user.UserEntity;

@Entity
@Table
public class BillEntity implements Serializable {
	private static final long serialVersionUID = 6731587709950299442L;

	/** ------------| instance |------------ **/

	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity exeBy;	
	
	private Status status= Status.New;
	
	private Date lastUpdatedOn= new Date();
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity lastUpdatedBy;

	@MapKey(name = "title")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, InstructionEntity> instructions;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)	
	private JobEntity job;
	
	/** ------------| setter-getter |------------ **/
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserEntity getExeBy() {
		return exeBy;
	}

	public void setExeBy(UserEntity exeBy) {
		this.exeBy = exeBy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public UserEntity getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(UserEntity lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Map<String, InstructionEntity> getInstructions() {
		return instructions;
	}

	public void setInstructions(Map<String, InstructionEntity> instructions) {
		this.instructions = instructions;
	}

	public JobEntity getJob() {
		return job;
	}

	public void setJob(JobEntity job) {
		this.job = job;
	}
}
