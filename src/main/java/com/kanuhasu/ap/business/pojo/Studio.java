package com.kanuhasu.ap.business.pojo;

import com.kanuhasu.ap.business.bo.job.Status;

public class Studio {
	private Long exeByID;
	private Status status;
	private Long id;

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
}
