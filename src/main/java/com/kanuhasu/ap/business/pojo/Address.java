package com.kanuhasu.ap.business.pojo;

import com.kanuhasu.ap.business.bo.user.AddressEntity;

public class Address {
	/** ------------| instance |------------ **/
	private String country;
	private String state;
	private String city;
	private int pincode;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String title;
	private String addressStr;
	private long id;
	private Long parentId;

	/** ------------| business |------------ **/

	public AddressEntity entity() {
		AddressEntity entity = new AddressEntity();
		entity.setAddressLine1(addressLine1);
		entity.setAddressLine2(addressLine2);
		entity.setAddressLine3(addressLine3);
		entity.setCity(city);
		entity.setCountry(country);
		entity.setPincode(pincode);
		entity.setState(state);
		entity.setTitle(title);
		entity.setId(id);
		entity.setAddressStr(addressStr);
		return entity;
	}

	/** ------------| setter-getter |------------ **/

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddressStr() {
		return addressStr;
	}

	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}