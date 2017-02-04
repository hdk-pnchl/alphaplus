package com.kanuhasu.ap.business.bo.complaint;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class PoliceStationEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9021222893210993045L;
	
	@Id
	@GeneratedValue
	private long id;
	private String name;
	
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
}
