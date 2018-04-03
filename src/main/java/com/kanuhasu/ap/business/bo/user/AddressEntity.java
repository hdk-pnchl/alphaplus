package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.kanuhasu.ap.business.bo.job.LastUpdateEntity;
import com.kanuhasu.ap.business.pojo.Address;
import com.kanuhasu.ap.business.util.CommonUtil;

@Entity
@Table(name = "Address")
public class AddressEntity extends LastUpdateEntity implements Serializable {
	private static final long serialVersionUID = 3410496272267921991L;

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

	/** ------------| constructor |------------ **/

	/** ------------| override |------------ **/

	@Override
	public String toString() {
		StringBuilder addressStrBuilding = new StringBuilder();
		addressStrBuilding.append("[ ").append(this.title).append(" : ").append(this.addressLine1).append(", ")
				.append(this.addressLine2).append(", ").append(this.addressLine3).append(", ").append(this.city)
				.append(", ").append(this.pincode).append(", ").append(this.state).append(", ").append(this.country)
				.append(" ]");

		return addressStrBuilding.toString();
	}

	/** ------------| business |------------ **/

	public Address pojo() {
		Address address = new Address();
		address.setAddressLine1(addressLine1);
		address.setAddressLine2(addressLine2);
		address.setAddressLine3(addressLine3);
		address.setAddressStr(addressStr);
		address.setCity(city);
		address.setCountry(country);
		address.setPincode(pincode);
		address.setState(state);
		address.setTitle(title);
		address.setId(this.getId());
		return address;
	}

	public void override(Address pojo) {
		this.setAddressLine1(pojo.getAddressLine1());
		this.setAddressLine2(pojo.getAddressLine2());
		this.setAddressLine3(pojo.getAddressLine3());
		this.setAddressStr(pojo.getAddressStr());
		this.setCity(pojo.getCity());
		this.setCountry(pojo.getCountry());
		this.setPincode(pojo.getPincode());
		this.setState(pojo.getState());
		this.setTitle(pojo.getTitle());
		this.setId(pojo.getId());
	}

	public static AddressEntity processParent(Address pojo, Set<AddressEntity> addresses) {
		AddressEntity currentAddress = null;
		if (CommonUtil.isIdDefined(pojo.getId())) {
			for (AddressEntity entity : addresses) {
				if (entity.getId() == pojo.getId()) {
					currentAddress = entity;
					currentAddress.override(pojo);
					break;
				}
			}
		} else {
			currentAddress = new AddressEntity();
			currentAddress.override(pojo);
			addresses.add(currentAddress);
		}
		return currentAddress;
	}

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

	public String getAddressStr() {
		return addressStr;
	}

	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
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
}