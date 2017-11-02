package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.type.bo.user.Unit;

@Entity
@Table
public class PlateEntity implements Serializable {
	private static final long serialVersionUID = 1585641668228221266L;
	
	/** ------------| instance |------------ **/
	
	@Id
	@GeneratedValue
	private long id;
	
	//plateSize
	private int plateHeight= 1;
	private int plateWidth= 1;
	private Unit plateUnit= Unit.CM;
	private String plateSize;
	
	//paperSize: 18*25 inch	
	private int paperHeight= 1;
	private int paperWidth= 1;
	private Unit paperUnit= Unit.CM;
	private String paperSize;
	
	//gripper: float
	private float gripper= 1;
	
	//screen: 3 digit no.
	private int screen= 1;
	
	//backing:
	//its less than or equal to total.
	//few among the set will be backed.
	//backing will have the no that will be backed.
	private int bake;
	
	//set
	private int theSet;
	private int theSetColour;
	private String setStr;
	
	//total: ==>set: 2-set 4-color=> 8 plates
	private int total;
	
	//total-form: F/B + S/B + D/G + OS
	private int frontBack;
	private int selfBack;
	private int doubleGripper;
	private int oneSide;
	private String fb_sb_dg_os;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private JobEntity job;
	
	private String title;
	
	private Date createdOn= new Date();
	
	private Status status = Status.New;
	
	private Date lastUpdatedOn= new Date();
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity lastUpdatedBy;
	
	/** ------------| constructor |------------**/
	
	/** ------------| business |------------**/

	/** ------------| getter-setter |------------**/
	
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
	
	public int getBake() {
		return bake;
	}
	
	public void setBake(int bake) {
		this.bake = bake;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public JobEntity getJob() {
		return job;
	}
	
	public void setJob(JobEntity job) {
		this.job = job;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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

	public String getPlateSize() {
		return plateSize;
	}

	public void setPlateSize(String plateSize) {
		this.plateSize = plateSize;
	}

	public String getPaperSize() {
		return paperSize;
	}

	public void setPaperSize(String paperSize) {
		this.paperSize = paperSize;
	}

	public String getSetStr() {
		return setStr;
	}

	public void setSetStr(String setStr) {
		this.setStr = setStr;
	}

	public String getFb_sb_dg_os() {
		return fb_sb_dg_os;
	}

	public void setFb_sb_dg_os(String fb_sb_dg_os) {
		this.fb_sb_dg_os = fb_sb_dg_os;
	}
	
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}	
}