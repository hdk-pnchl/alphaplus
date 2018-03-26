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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.pojo.Studio;

@Entity
@Table
public class StudioEntity implements Serializable {
	private static final long serialVersionUID = -8650896336233844585L;

	/** ------------| instance |------------ **/

	@Id
	@GeneratedValue
	private Long id;
	//
	@OneToOne(cascade = CascadeType.ALL)
	private UserEntity exeBy;
	private Status status = Status.New;
	// last
	private Date lastUpdatedOn = new Date();
	@OneToOne(cascade = CascadeType.ALL)
	private UserEntity lastUpdatedBy;
	// instructions
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "JOB_STUDIO_INSTRUCTION", joinColumns = @JoinColumn(name = "jobID"), inverseJoinColumns = @JoinColumn(name = "studioID"))
	private Set<InstructionEntity> instructions;
	// job
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	private JobEntity job;

	/** ------------| setter-getter |------------ **/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public JobEntity getJob() {
		return job;
	}

	public void setJob(JobEntity job) {
		this.job = job;
	}

	public Set<InstructionEntity> getInstructions() {
		return instructions;
	}

	public void setInstructions(Set<InstructionEntity> instructions) {
		this.instructions = instructions;
	}

	/** ------------| business |------------ **/

	public StudioEntity override(Studio ipStudio) {
		this.setStatus(ipStudio.getStatus());
		return this;
	}
}
