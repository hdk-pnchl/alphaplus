package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kanuhasu.ap.business.type.bo.user.Unit;

@Entity
@Table
public class SizeEntity implements Serializable {
	private static final long serialVersionUID = 7733930868272055170L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	
	private int height;
	private int width;
	private Unit unit;
	
	// setter-getter
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public Unit getUnit() {
		return unit;
	}
	
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
}
