package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.pojo.Address;
import com.kanuhasu.ap.business.pojo.Client;
import com.kanuhasu.ap.business.pojo.ClientBasic;
import com.kanuhasu.ap.business.pojo.Contact;

@Entity
@Table(name = "Client")
public class ClientEntity extends LastUpdateEntity implements Serializable {
	private static final long serialVersionUID = 5696903605244349608L;

	/** ------------| instance |------------ **/

	private String name;
	private String emailID;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "CLIENT_ADDRESS", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "addressID"))
	private Set<AddressEntity> addresses = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "CLIENT_CONTACT", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "contactID"))
	private Set<ContactEntity> contacts = new HashSet<>();

	/** ------------| constructor |------------ **/

	/** ------------| business |------------ **/

	public Client pojo() {
		Client pojo = new Client();
		pojo.setId(id);
		pojo.setEmailID(emailID);
		pojo.setName(name);
		return pojo;
	}

	public Client pojoFull() {
		Client pojo = this.pojo();
		// addresses
		Set<Address> addresses = pojo.getAddresses();
		for (AddressEntity entity : this.getAddresses()) {
			addresses.add(entity.pojo());
		}
		// contacts
		Set<Contact> contacts = pojo.getContacts();
		for (ContactEntity entity : this.getContacts()) {
			contacts.add(entity.pojo());
		}
		// core
		pojo.setLastUpdatedOn(this.getLastUpdatedOn());
		pojo.setCreatedOn(this.getCreatedOn());
		return pojo;
	}

	public void overrideBasic(ClientBasic basic) {
		this.setName(basic.getName());
		this.setEmailID(basic.getEmailID());
	}

	/** ------------| setter-getter |------------ **/

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

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public Set<AddressEntity> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<AddressEntity> addresses) {
		this.addresses = addresses;
	}

	public Set<ContactEntity> getContacts() {
		return contacts;
	}

	public void setContacts(Set<ContactEntity> contacts) {
		this.contacts = contacts;
	}
}