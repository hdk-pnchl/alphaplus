package com.kanuhasu.ap.business.pojo;

import com.kanuhasu.ap.business.bo.user.ContactEntity;

public class Contact {
	/** ------------| instance |------------ **/
	private String title;
	private String no;
	private String contactStr;
	private long id;
	private Long parentId;

	/** ------------| business |------------ **/

	public ContactEntity entity() {
		ContactEntity entity = new ContactEntity();
		entity.setTitle(this.title);
		entity.setNo(this.no);
		entity.setContactStr(this.contactStr);
		entity.setId(id);
		return entity;
	}

	/** ------------| setter-getter |------------ **/

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}