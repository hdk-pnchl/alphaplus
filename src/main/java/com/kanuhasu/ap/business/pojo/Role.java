package com.kanuhasu.ap.business.pojo;

import com.kanuhasu.ap.business.bo.user.RoleEntity;

public class Role {
	/** ------------| instance |------------ **/
	
	private String role;
	private Long id;

	/** ------------| business |------------ **/
	
	public RoleEntity entity() {
		RoleEntity entity= new RoleEntity();
		entity.setRole(role);
		entity.setId(id);
		return entity;
	}
	
	/** ------------| setter-getter |------------ **/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
