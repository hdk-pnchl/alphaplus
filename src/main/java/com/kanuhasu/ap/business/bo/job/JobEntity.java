package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.type.bo.user.BindingStyle;
import com.kanuhasu.ap.business.type.bo.user.ColorCopySize;
import com.kanuhasu.ap.business.util.CommonUtil;

@Entity
@Table
public class JobEntity implements Serializable {
	private static final long serialVersionUID = 5696903605244349608L;
	
	/** ------------| instance |------------**/

	@Id
	@GeneratedValue
	private long id;
	
	private String name;
	//pc-ap
	private String no;
	
	private Date receivedDate= new Date();
	private Date receivedTime= new Date();
	
	private Date targetDate= new Date();
	private Date targetTime= new Date();
	
	@ManyToOne(cascade = CascadeType.ALL)
	private ClientEntity client;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private AddressEntity deliveryAddress;	

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private AddressEntity billingAddress;	
	
	/* Instructions */
	
	@MapKey(name = "title")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, InstructionEntity> instructions;
	
	/* Plate Detail */
	
	private float cut= 1;
	private float open= 1;
	private int page= 1;
	//center-pinning, perfect-binding, section-binding, Wiro-binding 
	private BindingStyle bindingStyle= BindingStyle.CENTER;
	//Color copy size: A3/A4. Example: 2 of A3.
	private ColorCopySize colorCopySize= ColorCopySize.A4;
	
	/* Plate Detail : Plates */
	
	@MapKey(name = "title")
	@OneToMany(fetch = FetchType.EAGER)
	private Map<String, PlateEntity> plateDetail;
	
	/* Plate Detail : Internal*/
	
	//total should be "F/B + S/B + D/G + OS" or less
	private int frontBack;
	private int selfBack;
	private int doubleGripper;
	private int oneSide;
	private String fb_sb_dg_os;
	
	private int totalSet;
	private int totalPlates;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity docketBy;
	
	private Status docketStatus= Status.New;
	
	/* Functional Detail */
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	private StudioEntity studio= new StudioEntity(); 
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	private CTPEntity ctp= new CTPEntity(); 
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	private ChallanEntity challan= new ChallanEntity(); 
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	private DeliveryEntity delivery= new DeliveryEntity(); 

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	private BillEntity bill= new BillEntity(); 

	/* Core */
	
	private JobStatus status= JobStatus.NEW;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity lastUpdatedBy;
	private Date lastUpdatedOn= new Date();
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity closedBy;
	
	/** ------------| constructor |------------**/
	
	public JobEntity() {
		super();
	}
	
	/** ------------| business |------------**/
	
	/**
	 * This should not be used from anywhere other then JobDao
	 */
	public void populateNo() {
		this.setNo(CommonUtil.nextRegNo().toString());
	}
	
	/**
	 * This should not be used from anywhere other then JobDao
	 */
	public void processInternal(){
		if(this.getPlateDetail()!=null){
			for(Entry<String, PlateEntity> platEntry: this.getPlateDetail().entrySet()) {
				PlateEntity plate= platEntry.getValue();
				
				this.setFrontBack(this.getFrontBack()+plate.getFrontBack());
				this.setSelfBack(this.getSelfBack()+plate.getSelfBack());
				this.setDoubleGripper(this.getDoubleGripper()+this.getDoubleGripper());
				this.setOneSide(this.getOneSide()+plate.getOneSide());
				
				this.setTotalSet(this.getTotalSet()+plate.getTheSet());
				this.setTotalPlates(this.getTotalPlates()+plate.getTotal());
			}
		}
		this.populateFb_sb_dg_os();
	}
	
	/**
	 * This should not be used from anywhere other then processInternal
	 */
	private void populateFb_sb_dg_os(){
		this.setFb_sb_dg_os(this.getFrontBack()+"/"+this.getSelfBack()+"/"+this.getDoubleGripper()+"/"+this.getOneSide());
	}

	/** ------------| setter-getter |------------ **/
	
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

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Date getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Date getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(Date targetTime) {
		this.targetTime = targetTime;
	}

	public ClientEntity getClient() {
		return client;
	}

	public void setClient(ClientEntity client) {
		this.client = client;
	}

	public AddressEntity getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(AddressEntity deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public Map<String, InstructionEntity> getInstructions() {
		return instructions;
	}

	public void setInstructions(Map<String, InstructionEntity> instructions) {
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

	public Map<String, PlateEntity> getPlateDetail() {
		return plateDetail;
	}

	public void setPlateDetail(Map<String, PlateEntity> plateDetail) {
		this.plateDetail = plateDetail;
	}

	public int getFrontBack() {
		return frontBack;
	}

	public void setFrontBack(int frontBack) {
		this.frontBack = frontBack;
	}

	public int getSelfBack() {
		return selfBack;
	}

	public void setSelfBack(int selfBack) {
		this.selfBack = selfBack;
	}

	public int getDoubleGripper() {
		return doubleGripper;
	}

	public void setDoubleGripper(int doubleGripper) {
		this.doubleGripper = doubleGripper;
	}

	public int getOneSide() {
		return oneSide;
	}

	public void setOneSide(int oneSide) {
		this.oneSide = oneSide;
	}

	public String getFb_sb_dg_os() {
		return fb_sb_dg_os;
	}

	public void setFb_sb_dg_os(String fb_sb_dg_os) {
		this.fb_sb_dg_os = fb_sb_dg_os;
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

	public StudioEntity getStudio() {
		return studio;
	}

	public void setStudio(StudioEntity studio) {
		this.studio = studio;
	}

	public CTPEntity getCtp() {
		return ctp;
	}

	public void setCtp(CTPEntity ctp) {
		this.ctp = ctp;
	}

	public ChallanEntity getChallan() {
		return challan;
	}

	public void setChallan(ChallanEntity challan) {
		this.challan = challan;
	}

	public DeliveryEntity getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryEntity delivery) {
		this.delivery = delivery;
	}

	public BillEntity getBill() {
		return bill;
	}

	public void setBill(BillEntity bill) {
		this.bill = bill;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
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

	public AddressEntity getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(AddressEntity billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Status getDocketStatus() {
		return docketStatus;
	}

	public void setDocketStatus(Status docketStatus) {
		this.docketStatus = docketStatus;
	}	
	
	/** ------------| override |------------ **/
}