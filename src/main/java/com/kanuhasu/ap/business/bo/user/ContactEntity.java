package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.kanuhasu.ap.business.bo.job.LastUpdateEntity;
import com.kanuhasu.ap.business.pojo.Contact;
import com.kanuhasu.ap.business.util.CommonUtil;

@Entity
@Table(name = "Contact")
public class ContactEntity extends LastUpdateEntity implements Serializable {
	private static final long serialVersionUID = -2341973230214246584L;

	/** ------------| instance |------------ **/

	private String title;
	private String no;
	private String contactStr;

	/** ------------| override |------------ **/

	@Override
	public String toString() {
		StringBuilder strBuilding = new StringBuilder();
		strBuilding.append("[ ").append(this.title).append(" : ").append(this.no).append(" ]");

		return strBuilding.toString();
	}

	/** ------------| business |------------ **/

	public Contact pojo() {
		Contact pojo = new Contact();
		pojo.setTitle(this.title);
		pojo.setNo(this.no);
		pojo.setContactStr(this.contactStr);
		pojo.setId(id);
		return pojo;
	}

	public void override(Contact pojo) {
		this.setTitle(pojo.getTitle());
		this.setNo(pojo.getNo());
		this.setContactStr(pojo.getContactStr());
	}

	public static ContactEntity processParent(Contact pojo, Set<ContactEntity> contacts) {
		ContactEntity currentContact = null;
		if (CommonUtil.isIdDefined(pojo.getId())) {
			for (ContactEntity entity : contacts) {
				if (entity.getId() == pojo.getId()) {
					currentContact = entity;
					currentContact.override(pojo);
					break;
				}
			}
		} else {
			currentContact = new ContactEntity();
			currentContact.override(pojo);
			contacts.add(currentContact);
		}
		return currentContact;
	}

	/** ------------| setter-getter |------------ **/

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getContactStr() {
		return contactStr;
	}

	public void setContactStr(String contactStr) {
		this.contactStr = contactStr;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}