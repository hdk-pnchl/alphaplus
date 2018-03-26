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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.pojo.Delivery;

@Entity
@Table
public class DeliveryEntity implements Serializable {
	private static final long serialVersionUID = -6623312305589941602L;

	/** ------------| instance |------------ **/

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity exeBy;
	private Status status = Status.New;

	private Date date = new Date();
	private Date time = new Date();

	// last
	private Date lastUpdatedOn = new Date();
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity lastUpdatedBy;
	// inst
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "JOB_DELIVERY_INSTRUCTION", joinColumns = @JoinColumn(name = "jobID"), inverseJoinColumns = @JoinColumn(name = "deliveryID"))
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
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

	public DeliveryEntity override(Delivery ipDelivery) {
		this.setStatus(ipDelivery.getStatus());
		return this;
	}
}