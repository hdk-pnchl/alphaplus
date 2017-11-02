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
import com.kanuhasu.ap.business.util.CommonUtil;

@Entity
@Table
public class ChallanEntity implements Serializable {
	private static final long serialVersionUID = 7407199838414255161L;

	/** ------------| instance |------------ **/

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity exeBy;	
	
	private Date date= new Date();
	private String no;

	@MapKey(name = "title")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, InstructionEntity> instructions;

	private Status status = Status.New;

	private Date lastUpdatedOn = new Date();
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity lastUpdatedBy;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)	
	private JobEntity job;	

	/** ------------| business |------------ **/

	/**
	 * This should not be used from anywhere other then JobDao
	 */
	public void populateChallanNo() {
		this.setNo(CommonUtil.nextRegNo().toString());
	}

	/** ------------| setter-getter |------------ **/

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Map<String, InstructionEntity> getInstructions() {
		return instructions;
	}

	public void setInstructions(Map<String, InstructionEntity> instructions) {
		this.instructions = instructions;
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

	public UserEntity getExeBy() {
		return exeBy;
	}

	public void setExeBy(UserEntity exeBy) {
		this.exeBy = exeBy;
	}	
}