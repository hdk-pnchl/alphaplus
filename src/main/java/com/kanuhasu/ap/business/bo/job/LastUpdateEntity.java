package com.kanuhasu.ap.business.bo.job;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.kanuhasu.ap.business.bo.user.UserEntity;

@MappedSuperclass
public class LastUpdateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;
	private Date lastUpdatedOn = new Date();
	private long lastUpdatedById;
	@Transient
	private UserEntity lastUpdatedBy;
	private Date createdOn = new Date();

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public long getLastUpdatedById() {
		return lastUpdatedById;
	}

	public void setLastUpdatedById(long lastUpdatedById) {
		this.lastUpdatedById = lastUpdatedById;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public UserEntity getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(UserEntity lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}