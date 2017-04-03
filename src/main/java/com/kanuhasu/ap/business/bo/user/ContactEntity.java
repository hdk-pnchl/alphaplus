package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class ContactEntity implements Serializable {
	private static final long serialVersionUID = -2341973230214246584L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private String no;
	
	//constructor
	
	// setter-getter
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNo() {
		return no;
	}
	
	public void setNo(String no) {
		this.no = no;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	// override
}