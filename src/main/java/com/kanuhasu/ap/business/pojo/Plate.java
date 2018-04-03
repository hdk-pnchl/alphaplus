package com.kanuhasu.ap.business.pojo;

import java.util.Date;

import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.bo.job.Status;
import com.kanuhasu.ap.business.type.bo.user.Unit;

public class Plate {
	/** ------------| instance |------------ **/
	private long id;
	// plateSize
	private int plateHeight = 1;
	private int plateWidth = 1;
	private Unit plateUnit = Unit.CM;
	private String plateSize;
	// paperSize: 18*25 inch
	private int paperHeight = 1;
	private int paperWidth = 1;
	private Unit paperUnit = Unit.CM;
	private String paperSize;
	// gripper: float
	private float gripper = 1;
	// screen: 3 digit no.
	private int screen = 1;
	private String gripper_screen;
	private int bake;
	// set
	private int theSet;
	private int theSetColour;
	private String setStr;
	// total: ==>set: 2-set 4-color=> 8 plates
	private int total;
	// total-form: F/B + S/B + D/G + OS
	private int frontBack;
	private int selfBack;
	private int doubleGripper;
	private int oneSide;
	private String fb_sb_dg_os;
	// core
	private Status status = Status.New;
	private String title;

	private Date lastUpdatedOn = new Date();
	private Date createdOn = new Date();
	private Long jobID;

	/** ------------| business |------------ **/

	public PlateEntity entity() {
		PlateEntity entity = new PlateEntity();
		entity.setTitle(this.getTitle());
		entity.setStatus(this.getStatus());

		entity.setDoubleGripper(this.getDoubleGripper());
		entity.setFrontBack(this.getFrontBack());
		entity.setOneSide(this.getOneSide());
		entity.setFb_sb_dg_os(this.getFb_sb_dg_os());
		entity.setSelfBack(this.getSelfBack());

		entity.setPaperHeight(this.getPaperHeight());
		entity.setPaperSize(this.getPaperSize());
		entity.setPaperUnit(this.getPaperUnit());
		entity.setPaperWidth(this.getPaperWidth());

		entity.setPlateHeight(this.getPlateHeight());
		entity.setPlateSize(this.getPlateSize());
		entity.setPlateUnit(this.getPlateUnit());
		entity.setPlateWidth(this.getPlateWidth());

		entity.setGripper(this.getGripper());
		entity.setScreen(this.getScreen());
		entity.setGripper_screen(this.getGripper_screen());

		entity.setBake(this.getBake());
		entity.setTotal(this.getTotal());
		entity.setTheSetColour(this.getTheSetColour());
		entity.setTheSet(this.getTheSet());
		entity.setSetStr(this.getSetStr());

		return entity;
	}

	/** ------------| getter-setter |------------ **/

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPlateHeight() {
		return plateHeight;
	}

	public void setPlateHeight(int plateHeight) {
		this.plateHeight = plateHeight;
	}

	public int getPlateWidth() {
		return plateWidth;
	}

	public void setPlateWidth(int plateWidth) {
		this.plateWidth = plateWidth;
	}

	public Unit getPlateUnit() {
		return plateUnit;
	}

	public void setPlateUnit(Unit plateUnit) {
		this.plateUnit = plateUnit;
	}

	public String getPlateSize() {
		return plateSize;
	}

	public void setPlateSize(String plateSize) {
		this.plateSize = plateSize;
	}

	public int getPaperHeight() {
		return paperHeight;
	}

	public void setPaperHeight(int paperHeight) {
		this.paperHeight = paperHeight;
	}

	public int getPaperWidth() {
		return paperWidth;
	}

	public void setPaperWidth(int paperWidth) {
		this.paperWidth = paperWidth;
	}

	public Unit getPaperUnit() {
		return paperUnit;
	}

	public void setPaperUnit(Unit paperUnit) {
		this.paperUnit = paperUnit;
	}

	public String getPaperSize() {
		return paperSize;
	}

	public void setPaperSize(String paperSize) {
		this.paperSize = paperSize;
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

	public String getGripper_screen() {
		return gripper_screen;
	}

	public void setGripper_screen(String gripper_screen) {
		this.gripper_screen = gripper_screen;
	}

	public int getBake() {
		return bake;
	}

	public void setBake(int bake) {
		this.bake = bake;
	}

	public int getTheSet() {
		return theSet;
	}

	public void setTheSet(int theSet) {
		this.theSet = theSet;
	}

	public int getTheSetColour() {
		return theSetColour;
	}

	public void setTheSetColour(int theSetColour) {
		this.theSetColour = theSetColour;
	}

	public String getSetStr() {
		return setStr;
	}

	public void setSetStr(String setStr) {
		this.setStr = setStr;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getFrontBack() {
		return frontBack;
	}

	public void setFrontBack(int frontBack) {
		this.frontBack = frontBack;
	}

	public int getSelfBack() {
		return selfBack;
	}

	public void setSelfBack(int selfBack) {
		this.selfBack = selfBack;
	}

	public int getDoubleGripper() {
		return doubleGripper;
	}

	public void setDoubleGripper(int doubleGripper) {
		this.doubleGripper = doubleGripper;
	}

	public int getOneSide() {
		return oneSide;
	}

	public void setOneSide(int oneSide) {
		this.oneSide = oneSide;
	}

	public String getFb_sb_dg_os() {
		return fb_sb_dg_os;
	}

	public void setFb_sb_dg_os(String fb_sb_dg_os) {
		this.fb_sb_dg_os = fb_sb_dg_os;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getJobID() {
		return jobID;
	}

	public void setJobID(Long jobID) {
		this.jobID = jobID;
	}
}