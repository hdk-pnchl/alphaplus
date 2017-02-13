package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class SetEntity implements Serializable {
	private static final long serialVersionUID = -58213511147729752L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	
	private int quantity;
	private int colorCount;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "plate")
	private PlateEntity plate;
	
	// setter-getter
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getColorCount() {
		return colorCount;
	}
	
	public void setColorCount(int colorCount) {
		this.colorCount = colorCount;
	}
	
	public PlateEntity getPlate() {
		return plate;
	}
	
	public void setPlate(PlateEntity plate) {
		this.plate = plate;
	}
}