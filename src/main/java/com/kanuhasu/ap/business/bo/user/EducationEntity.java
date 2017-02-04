package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.bo.user.type.EducationModeType;

@Entity
@Table
public class EducationEntity implements Serializable {
	private static final long serialVersionUID = 5804049992570803543L;
	
	// Instance
	
	@Id
	@GeneratedValue
	private long id;
	
	private EducationModeType educationMode= EducationModeType.OTHER;
	private String educationModeDesc;
	
	private boolean bachelors;
	private String bachelorsDesc;
	
	private boolean masters;
	private String mastersDesc;
	
	private boolean phd;
	private String phdDesc;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "user")
	private UserEntity user;
	
	// constructor
	
	// setter-getter
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public EducationModeType getEducationMode() {
		return educationMode;
	}
	
	public void setEducationMode(EducationModeType educationMode) {
		this.educationMode = educationMode;
	}
	
	public String getEducationModeDesc() {
		return educationModeDesc;
	}
	
	public void setEducationModeDesc(String educationModeDesc) {
		this.educationModeDesc = educationModeDesc;
	}
	
	public boolean isBachelors() {
		return bachelors;
	}
	
	public void setBachelors(boolean bachelors) {
		this.bachelors = bachelors;
	}
	
	public String getBachelorsDesc() {
		return bachelorsDesc;
	}
	
	public void setBachelorsDesc(String bachelorsDesc) {
		this.bachelorsDesc = bachelorsDesc;
	}
	
	public boolean isMasters() {
		return masters;
	}
	
	public void setMasters(boolean masters) {
		this.masters = masters;
	}
	
	public String getMastersDesc() {
		return mastersDesc;
	}
	
	public void setMastersDesc(String mastersDesc) {
		this.mastersDesc = mastersDesc;
	}
	
	public boolean isPhd() {
		return phd;
	}
	
	public void setPhd(boolean phd) {
		this.phd = phd;
	}
	
	public String getPhdDesc() {
		return phdDesc;
	}
	
	public void setPhdDesc(String phdDesc) {
		this.phdDesc = phdDesc;
	}
	
	public UserEntity getUser() {
		return user;
	}
	
	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	// override
}
