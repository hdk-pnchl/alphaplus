package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class SetDetailEntity implements Serializable {
	private static final long serialVersionUID = -6732661045511178482L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	
	private int F_B;
	private int S_B;
	private int D_G;
	private int O_S;
	
	// setter-getter
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getF_B() {
		return F_B;
	}
	
	public void setF_B(int f_B) {
		F_B = f_B;
	}
	
	public int getS_B() {
		return S_B;
	}
	
	public void setS_B(int s_B) {
		S_B = s_B;
	}
	
	public int getD_G() {
		return D_G;
	}
	
	public void setD_G(int d_G) {
		D_G = d_G;
	}
	
	public int getO_S() {
		return O_S;
	}
	
	public void setO_S(int o_S) {
		O_S = o_S;
	}
	
	// constructor
	
	// override
}
