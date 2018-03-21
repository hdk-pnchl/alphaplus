package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class AddressEntity implements Serializable {
	private static final long serialVersionUID = 3410496272267921991L;
	
	/** ------------| instance |------------ **/
	
	@Id
	@GeneratedValue
	private long id;
	private String country;
	private String state;
	private String city;
	private int pincode;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String title;
	private String addressStr; 
	
	private Date createdOn= new Date();
	private Date lastUpdatedOn= new Date();
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity lastUpdatedBy;
	
	
	/** ------------| constructor |------------ **/

	/** ------------| setter-getter |------------ **/
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public int getPincode() {
		return pincode;
	}
	
	public void setPincode(int pincode) {
		this.pincode = pincode;
	}
	
	public String getAddressLine1() {
		return addressLine1;
	}
	
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	
	public String getAddressLine2() {
		return addressLine2;
	}
	
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	
	public String getAddressLine3() {
		return addressLine3;
	}
	
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getAddressStr() {
		return addressStr;
	}

	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** ------------| override |------------ **/

	@Override
	public String toString() {
		StringBuilder addressStrBuilding= new StringBuilder();
		addressStrBuilding
		.append("[ ")
		.append(this.title).append(" : ")
		.append(this.addressLine1).append(", ")
		.append(this.addressLine2).append(", ")
		.append(this.addressLine3).append(", ")
		.append(this.city).append(", ")
		.append(this.pincode).append(", ")
		.append(this.state).append(", ")
		.append(this.country).append(" ]");
		
		return addressStrBuilding.toString();
	}
}