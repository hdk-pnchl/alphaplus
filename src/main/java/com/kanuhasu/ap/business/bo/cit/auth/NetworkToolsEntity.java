package com.kanuhasu.ap.business.bo.cit.auth;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CarrierToolsRecord")
public class NetworkToolsEntity implements Serializable {
	private static final long serialVersionUID = -5160633223064078161L;

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String networkCode;

	@Column
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<ToolEntity> tools;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNetworkCode() {
		return networkCode;
	}

	public void setNetworkCode(String networkCode) {
		this.networkCode = networkCode;
	}

	public Set<ToolEntity> getTools() {
		return tools;
	}

	public void setTools(Set<ToolEntity> tools) {
		this.tools = tools;
	}
}
