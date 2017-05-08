package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.type.bo.user.BindingStyle;
import com.kanuhasu.ap.business.type.bo.user.ColorCopySize;

@Entity
@Table
public class JobEntity implements Serializable {
	private static final long serialVersionUID = 5696903605244349608L;
	
	// instance
	
	@Id
	@GeneratedValue
	private long id;
	
	/* Basic Detail */
	
	private String name;
	//pc - ap
	private long no;
	
	private Date receivedDate;
	private String receivedTime;
	
	private Date targetDate;
	private String targetTime;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private ClientEntity client;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "job")
	private List<JobInstEntity> instructions;
	
	/* Plate Detail */
	
	private float cut;
	private float open;
	private int page;
	//center-pinning, perfect-binding, section-binding, Wiro-binding 
	private BindingStyle bindingStyle;
	//Color copy size: A3/A4. Example: 2 of A3.
	private ColorCopySize colorCopySize;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "job")
	private List<PlateEntity> plates;
	
	//total-form: F/B + S/B + D/G + OS
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private SetDetailEntity setDetail;
	
	private int totalSet;
	private int totalPlates;
	
	/* Functional Detail */
	
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity docketBy;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity ripBy;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity processBy;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity plateBy;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity challanBy;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity lastUpdatedBy;
	private Date lastUpdatedOn;
	
	/* Delivery Detail */
	
	private Date deliveryDate;
	private String deliveryTime;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private AddressEntity deliveryAddress;
	
	private Date challanDate;
	private String challanNo;
	
	// constructor
	
	// setter-getter
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getNo() {
		return no;
	}
	
	public void setNo(long no) {
		this.no = no;
	}
	
	public Date getReceivedDate() {
		return receivedDate;
	}
	
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	public String getReceivedTime() {
		return receivedTime;
	}
	
	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}
	
	public Date getTargetDate() {
		return targetDate;
	}
	
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	
	public String getTargetTime() {
		return targetTime;
	}
	
	public void setTargetTime(String targetTime) {
		this.targetTime = targetTime;
	}
	
	public ClientEntity getClient() {
		return client;
	}
	
	public void setClient(ClientEntity client) {
		this.client = client;
	}
	
	public List<JobInstEntity> getInstructions() {
		return instructions;
	}
	
	public void setInstructions(List<JobInstEntity> instructions) {
		this.instructions = instructions;
	}
	
	public float getCut() {
		return cut;
	}
	
	public void setCut(float cut) {
		this.cut = cut;
	}
	
	public float getOpen() {
		return open;
	}
	
	public void setOpen(float open) {
		this.open = open;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public BindingStyle getBindingStyle() {
		return bindingStyle;
	}
	
	public void setBindingStyle(BindingStyle bindingStyle) {
		this.bindingStyle = bindingStyle;
	}
	
	public ColorCopySize getColorCopySize() {
		return colorCopySize;
	}
	
	public void setColorCopySize(ColorCopySize colorCopySize) {
		this.colorCopySize = colorCopySize;
	}
	
	public List<PlateEntity> getPlates() {
		return plates;
	}
	
	public void setPlates(List<PlateEntity> plates) {
		this.plates = plates;
	}
	
	public SetDetailEntity getSetDetail() {
		return setDetail;
	}
	
	public void setSetDetail(SetDetailEntity setDetail) {
		this.setDetail = setDetail;
	}
	
	public int getTotalSet() {
		return totalSet;
	}
	
	public void setTotalSet(int totalSet) {
		this.totalSet = totalSet;
	}
	
	public int getTotalPlates() {
		return totalPlates;
	}
	
	public void setTotalPlates(int totalPlates) {
		this.totalPlates = totalPlates;
	}
	
	public UserEntity getDocketBy() {
		return docketBy;
	}
	
	public void setDocketBy(UserEntity docketBy) {
		this.docketBy = docketBy;
	}
	
	public UserEntity getRipBy() {
		return ripBy;
	}
	
	public void setRipBy(UserEntity ripBy) {
		this.ripBy = ripBy;
	}
	
	public UserEntity getProcessBy() {
		return processBy;
	}
	
	public void setProcessBy(UserEntity processBy) {
		this.processBy = processBy;
	}
	
	public UserEntity getPlateBy() {
		return plateBy;
	}
	
	public void setPlateBy(UserEntity plateBy) {
		this.plateBy = plateBy;
	}
	
	public UserEntity getChallanBy() {
		return challanBy;
	}
	
	public void setChallanBy(UserEntity challanBy) {
		this.challanBy = challanBy;
	}
	
	public UserEntity getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	
	public void setLastUpdatedBy(UserEntity lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public String getDeliveryTime() {
		return deliveryTime;
	}
	
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	
	public AddressEntity getDeliveryAddress() {
		return deliveryAddress;
	}
	
	public void setDeliveryAddress(AddressEntity deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	
	public Date getChallanDate() {
		return challanDate;
	}
	
	public void setChallanDate(Date challanDate) {
		this.challanDate = challanDate;
	}
	
	public String getChallanNo() {
		return challanNo;
	}
	
	public void setChallanNo(String challanNo) {
		this.challanNo = challanNo;
	}
	
	// override
}