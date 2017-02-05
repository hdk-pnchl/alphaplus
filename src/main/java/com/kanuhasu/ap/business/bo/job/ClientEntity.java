package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kanuhasu.ap.business.bo.user.AddressEntity;

@Entity
@Table
public class ClientEntity implements Serializable {
	private static final long serialVersionUID = 5696903605244349608L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	private String name;
	
    @ElementCollection
    @MapKeyColumn(name="type")
    @Column(name="contact")
    @CollectionTable(name="client_contact", joinColumns=@JoinColumn(name="client_id"))
    Map<String, String> contactDetail = new HashMap<String, String>(); // maps from attribute name to value

	@MapKey(name = "name")
	@OneToMany(fetch = FetchType.LAZY)
	private Map<String, AddressEntity> addressDetail;

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

	public Map<String, String> getContactDetail() {
		return contactDetail;
	}

	public void setContactDetail(Map<String, String> contactDetail) {
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