package com.kanuhasu.ap.business.bo.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.kanuhasu.ap.business.bo.job.LastUpdateEntity;
import com.kanuhasu.ap.business.pojo.Role;

@Entity
@Table(name = "Role")
public final class RoleEntity extends LastUpdateEntity implements Serializable {
	/** ------------| instance |------------ **/

	private static final long serialVersionUID = 3349613084505785425L;

	private String role;

	// constructor

	public RoleEntity() {
	}

	public RoleEntity(String role) {
		Assert.hasText(role, "A granted authority textual representation is required");
		this.role = role;
	}

	/** ------------| business |------------ **/

	public Role pojo() {
		Role role = new Role();
		role.setRole(this.role);
		role.setId(id);
		return role;
	}

	/** ------------| override |------------ **/

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof RoleEntity) {
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

	/** ------------| setter-getter |------------ **/

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}