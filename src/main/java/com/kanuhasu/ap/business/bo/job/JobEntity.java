package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;

@Entity
@Table
public class JobEntity implements Serializable {
	private static final long serialVersionUID = 5696903605244349608L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	
	private long no;
	//pc - ap
	
	private Date receivedDate;
	private Date receivedTime;
	
	private Date targetDate;
	private Date targetTime;
	
	private Date deliveryDate;
	private Date deliveryTime;
	
	private Date lastUpdatedOn;
	private UserEntity lastUpdatedBy;
	
	private ClientEntity client;
	
	private String name;
	
	private AddressEntity deliveryAddress;
	
	private UserEntity dockeBy;
	private UserEntity ripBy;
	private UserEntity processBy;
	private UserEntity plateBy;
	
	private String challanNo;
	private Date challanDate;
	private UserEntity challanBy;
	
	private String cut;
	private String open;
	private String page;
	private String bindingStyle;
	
	//plateSize, gripper, screen, paperSize, set, back, total
	
	// constructor
	
	// setter-getter
	
	// override
	
}
