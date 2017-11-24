package com.kanuhasu.ap.business.bo.cit.auth;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="OperationRecord")
public class OperationEntity implements GrantedAuthority, Serializable {
	private static final long serialVersionUID = -3660048542940851050L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
    private String name;

    @Column
    private String label;

    @ManyToOne(cascade = CascadeType.ALL)
    public CITRoleEntity role;

    @Override
    public String getAuthority() {
        return this.getName();
    }

    public String getName(){
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

    public CITRoleEntity getRole() {
        return role;
    }

    public void setRole(CITRoleEntity role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "OperationEntity{" +
                "name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", role=" + role +
                '}';
    }
}
