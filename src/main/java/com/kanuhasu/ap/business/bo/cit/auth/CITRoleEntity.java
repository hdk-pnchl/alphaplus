package com.kanuhasu.ap.business.bo.cit.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "RoleRecord")
public class CITRoleEntity implements GrantedAuthority, Serializable {
	private static final long serialVersionUID = -5532574615050457300L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;

	@Column
	private String label;

	@Column
	private String description;

	@Override
	public String getAuthority() {
		return this.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "RoleEntity{" + "name='" + name + '\'' + ", label=" + label + ", description=" + description + '}';
	}
}
