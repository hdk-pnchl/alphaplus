package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.kanuhasu.ap.business.pojo.Instruction;

@Entity
@Table(name = "Instruction")
public class InstructionEntity extends LastUpdateEntity implements Serializable {
	private static final long serialVersionUID = -3144173378364863320L;

	/** ------------| instance |------------ **/

	private String value;
	private String title;

	/** ------------| business |------------ **/

	public void override(Instruction instruction) {
		this.setTitle(instruction.getTitle());
		this.setValue(instruction.getValue());
	}

	public Instruction pojo() {
		Instruction pojo = new Instruction();
		pojo.setId(id);
		pojo.setTitle(title);
		pojo.setValue(value);
		return pojo;
	}

	public Instruction pojoFull() {
		Instruction pojo = this.pojo();
		pojo.setCreatedOn(this.getCreatedOn());
		pojo.setLastUpdatedOn(this.getLastUpdatedOn());
		return pojo;
	}

	/** ------------| setter-getter |------------ **/

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}