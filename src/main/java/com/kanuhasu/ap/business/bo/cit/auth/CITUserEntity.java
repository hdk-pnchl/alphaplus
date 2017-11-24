package com.kanuhasu.ap.business.bo.cit.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "UserRecord")
public class CITUserEntity implements UserDetails {
	private static final long serialVersionUID = -8695813648512752911L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String userName;
	@Column
	private String emailID;
	@Column
	private String password;

	@Column
	private boolean accountNonExpired;
	@Column
	private boolean accountNonLocked;
	@Column
	private boolean credentialsNonExpired;
	@Column
	private boolean enabled;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<NetworkEntity> networks;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<CITRoleEntity> roles;

	@MapKey(name = "networkCode")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Map<String, NetworkToolsEntity> networkTools;

	
	@Column
	private String lastUpdatedBy;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for(CITRoleEntity role: roles) {
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));			
		}
		return authorities;
	}

	@Override
	public String getUsername() {
		return this.getEmailID();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<NetworkEntity> getNetworks() {
		return networks;
	}

	public void setNetworks(Set<NetworkEntity> networks) {
		this.networks = networks;
	}

	public Set<CITRoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<CITRoleEntity> roles) {
		this.roles = roles;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Map<String, NetworkToolsEntity> getNetworkTools() {
		return networkTools;
	}

	public void setNetworkTools(Map<String, NetworkToolsEntity> networkTools) {
		this.networkTools = networkTools;
	}
}
