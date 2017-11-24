package com.kanuhasu.ap.business.bo.cit.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ToolRecord")
public class ToolEntity implements Serializable {
	private static final long serialVersionUID = 6554069705328211660L;

	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String name;
	@Column
	private String description;
	@Column
	private String label;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "ToolEntity{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", label='" + label
				+ "'" + '}';
	}
}
