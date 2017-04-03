package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class PlateEntity implements Serializable {
	private static final long serialVersionUID = 1585641668228221266L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	
	//plateSize: 18*25 mm
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private SizeEntity plateSize;
	//gripper: float
	private float gripper;
	//screen: 3 digit no.
	private int screen;
	//paperSize: 18*25 inch
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private SizeEntity paperSize;
	//set: 2-set 4-color=> 8 plates.
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plate")
	private SetEntity set;
	//backing:
	//its less than or equal to total.
	//few among the set will be backed.
	//backing will have the no that will be backed.
	private int backCount;
	//total: ==>set: 2-set 4-color=> 8 plates
	private int total;
	//total-form: F/B + S/B + D/G + OS
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private SetDetailEntity setDetail;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private JobEntity job;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public SizeEntity getPlateSize() {
		return plateSize;
	}
	
	public void setPlateSize(SizeEntity plateSize) {
		this.plateSize = plateSize;
	}
	
	public float getGripper() {
		return gripper;
	}
	
	public void setGripper(float gripper) {
		this.gripper = gripper;
	}
	
	public int getScreen() {
		return screen;
	}
	
	public void setScreen(int screen) {
		this.screen = screen;
	}
	
	public SizeEntity getPaperSize() {
		return paperSize;
	}
	
	public void setPaperSize(SizeEntity paperSize) {
		this.paperSize = paperSize;
	}
	
	public SetEntity getSet() {
		return set;
	}
	
	public void setSet(SetEntity set) {
		this.set = set;
	}
	
	public int getBackCount() {
		return backCount;
	}
	
	public void setBackCount(int backCount) {
		this.backCount = backCount;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public SetDetailEntity getSetDetail() {
		return setDetail;
	}
	
	public void setSetDetail(SetDetailEntity setDetail) {
		this.setDetail = setDetail;
	}
	
	public JobEntity getJob() {
		return job;
	}
	
	public void setJob(JobEntity job) {
		this.job = job;
	}
	
	// constructor
	
}