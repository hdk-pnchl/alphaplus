package com.kanuhasu.ap.business.pojo;

import java.util.Date;

import com.kanuhasu.ap.business.bo.job.Status;
import com.kanuhasu.ap.business.type.bo.user.BindingStyle;
import com.kanuhasu.ap.business.type.bo.user.ColorCopySize;

public class Docket {

	/** ------------| instance |------------ **/

	private Long jobID;
	private String name;
	private String no;
	private Date receivedDate = new Date();
	private Date targetDate = new Date();
	private float cut;
	private float open;
	private int page;
	private BindingStyle bindingStyle;
	private ColorCopySize colorCopySize;
	private Status docketStatus;
	private long docketByID;
	private long clientID;

	/** ------------| setter-getter |------------ **/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public long getClientID() {
		return clientID;
	}

	public void setClientID(long clientID) {
		this.clientID = clientID;
	}

	public float getCut() {
		return cut;
	}

	public void setCut(float cut) {
		this.cut = cut;
	}

	public float getOpen() {
		return open;
	}

	public void setOpen(float open) {
		this.open = open;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public BindingStyle getBindingStyle() {
		return bindingStyle;
	}

	public void setBindingStyle(BindingStyle bindingStyle) {
		this.bindingStyle = bindingStyle;
	}

	public ColorCopySize getColorCopySize() {
		return colorCopySize;
	}

	public void setColorCopySize(ColorCopySize colorCopySize) {
		this.colorCopySize = colorCopySize;
	}

	public Status getDocketStatus() {
		return docketStatus;
	}

	public void setDocketStatus(Status docketStatus) {
		this.docketStatus = docketStatus;
	}

	public long getDocketByID() {
		return docketByID;
	}

	public void setDocketByID(long docketByID) {
		this.docketByID = docketByID;
	}

	public Long getJobID() {
		return jobID;
	}

	public void setJobID(Long jobID) {
		this.jobID = jobID;
	}
}