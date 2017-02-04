package com.kanuhasu.ap.business.type.bo.user;

public enum Roles {
	GUEST("ROLE_GUEST"), 
	ADMIN("ROLE_ADMIN"), 
	MEMBER("ROLE_MEMBER");

	Roles(String name) {
		this.name = name;
	}

	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Roles fetchRoleByName(String name) {
		for (Roles role : Roles.values()) {
			if (role.getName().equals(name)) {
				return role;
			}
		}
		return null;
	}
}
