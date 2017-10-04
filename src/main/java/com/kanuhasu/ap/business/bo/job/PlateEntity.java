package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	private int plateHeight;
	private int plateWidth;
	private Unit plateUnit;
	
	//paperSize: 18*25 inch	
	private int paperHeight;
	private int paperWidth;
	private Unit paperUnit;
	
	//gripper: float
	private float gripper;
	
	//screen: 3 digit no.
	private int screen;
	
	//set
	private int theSet;
	private int theSetColour;
	
	//backing:
	//its less than or equal to total.
	//few among the set will be backed.
	//backing will have the no that will be backed.
	private int bake;
	
	//total: ==>set: 2-set 4-color=> 8 plates
	private int total;
	
	//total-form: F/B + S/B + D/G + OS
	private int F_B;
	private int S_B;
	private int D_G;
	private int O_S;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private JobEntity job;
	
	private String title;
	
	/** ------------| constructor |------------**/
	
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
	
	public int getF_B() {
		return F_B;
	}
	
	public void setF_B(int f_B) {
		F_B = f_B;
	}
	
	public int getS_B() {
		return S_B;
	}
	
	public void setS_B(int s_B) {
		S_B = s_B;
	}
	
	public int getD_G() {
		return D_G;
	}
	
	public void setD_G(int d_G) {
		D_G = d_G;
	}
	
	public int getO_S() {
		return O_S;
	}
	
	public void setO_S(int o_S) {
		O_S = o_S;
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
}