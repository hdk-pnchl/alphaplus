package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.ContactEntity;

@Entity
@Table
public class ClientEntity implements Serializable {
	private static final long serialVersionUID = 5696903605244349608L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	private String name;
	
	@MapKey(name = "name")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, AddressEntity> addressDetail;
	
	@MapKey(name = "name")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, ContactEntity> contactDetail;
	
	// setter-getter
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, ContactEntity> getContactDetail() {
		return contactDetail;
	}
	
	public void setContactDetail(Map<String, ContactEntity> contactDetail) {
		this.contactDetail = contactDetail;
	}
	
	public Map<String, AddressEntity> getAddressDetail() {
		return addressDetail;
	}
	
	public void setAddressDetail(Map<String, AddressEntity> addressDetail) {
		this.addressDetail = addressDetail;
	}
	
	// constructor
	
	// override
}