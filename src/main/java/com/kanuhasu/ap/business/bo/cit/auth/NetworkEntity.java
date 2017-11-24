package com.kanuhasu.ap.business.bo.cit.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NetworkRecord")
public class NetworkEntity implements Serializable {
	private static final long serialVersionUID = -8857922519904541035L;

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String networkCode;
	@Column
	private String country;

	public String getNetworkCode() {
		return networkCode;
	}

	public void setNetworkCode(String networkCode) {
		this.networkCode = networkCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "NetworkEntity{" + "code='" + networkCode + '\'' + ", country='" + country + '\'' + '}';
	}
}
