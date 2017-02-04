package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.Assert;

@Entity
@Table
public final class RoleEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3349613084505785425L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	private String role;
	
	//constructor
	
	public RoleEntity() {
	}
	
	public RoleEntity(String role) {
		Assert.hasText(role, "A granted authority textual representation is required");
		this.role = role;
	}
	
	// setter-getter
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	// override
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		
		if(obj instanceof RoleEntity) {
			return role.equals(((RoleEntity) obj).role);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.role.hashCode();
	}
	
	@Override
	public String toString() {
		return this.role;
	}
}
