package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.bo.user.type.IndustryType;
import com.kanuhasu.ap.business.bo.user.type.ProfessionType;
import com.kanuhasu.ap.business.type.bo.user.WorkType;

@Entity
@Table
public class OccupationEntity implements Serializable {
	private static final long serialVersionUID = 5804049992570803543L;
	
	// Instance
	
	@Id
	@GeneratedValue
	private long id;
	
	private ProfessionType professionType= ProfessionType.OTHER;
	private String professionTypeDesc;
	
	private WorkType workType= WorkType.OTHER;
	private String workTypeDesc;
	
	private IndustryType industryType= IndustryType.OTHER;
	private String industryTypeDesc;
	
	private String gereralDesc;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "user")
	private UserEntity user;
	
	//constructor
	
	// setter-getter
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public ProfessionType getProfessionType() {
		return professionType;
	}
	
	public void setProfessionType(ProfessionType professionType) {
		this.professionType = professionType;
	}
	
	public String getProfessionTypeDesc() {
		return professionTypeDesc;
	}
	
	public void setProfessionTypeDesc(String professionTypeDesc) {
		this.professionTypeDesc = professionTypeDesc;
	}
	
	public WorkType getWorkType() {
		return workType;
	}
	
	public void setWorkType(WorkType workType) {
		this.workType = workType;
	}
	
	public String getWorkTypeDesc() {
		return workTypeDesc;
	}
	
	public void setWorkTypeDesc(String workTypeDesc) {
		this.workTypeDesc = workTypeDesc;
	}
	
	public IndustryType getIndustryType() {
		return industryType;
	}
	
	public void setIndustryType(IndustryType industryType) {
		this.industryType = industryType;
	}
	
	public String getIndustryTypeDesc() {
		return industryTypeDesc;
	}
	
	public void setIndustryTypeDesc(String industryTypeDesc) {
		this.industryTypeDesc = industryTypeDesc;
	}
	
	public String getGereralDesc() {
		return gereralDesc;
	}
	
	public void setGereralDesc(String gereralDesc) {
		this.gereralDesc = gereralDesc;
	}
	
	public UserEntity getUser() {
		return user;
	}
	
	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	// override
}
