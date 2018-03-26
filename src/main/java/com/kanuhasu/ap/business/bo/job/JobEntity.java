package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
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

	/** ------------| instance |------------ **/

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	// pc-ap
	private String no;

	private Date receivedDate = new Date();
	private Date receivedTime = new Date();

	private Date targetDate = new Date();
	private Date targetTime = new Date();

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private ClientEntity client;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private AddressEntity deliveryAddress;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private AddressEntity billingAddress;

	/* Instructions */

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "JOB_INSTRUCTION", joinColumns = @JoinColumn(name = "jobID"), inverseJoinColumns = @JoinColumn(name = "instructionID"))
	private Set<InstructionEntity> instructions;

	/* Plate Detail */

	private float cut = 1;
	private float open = 1;
	private int page = 1;
	// center-pinning, perfect-binding, section-binding, Wiro-binding
	private BindingStyle bindingStyle = BindingStyle.CENTER;
	// Color copy size: A3/A4. Example: 2 of A3.
	private ColorCopySize colorCopySize = ColorCopySize.A4;

	/* Plate Detail : Plates */

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "JOB_PLATE", joinColumns = @JoinColumn(name = "jobID"), inverseJoinColumns = @JoinColumn(name = "plateID"))
	private Set<PlateEntity> plates;

	/* Plate Detail : Internal */

	// total should be "F/B + S/B + D/G + OS" or less
	private int frontBack;
	private int selfBack;
	private int doubleGripper;
	private int oneSide;
	private String fb_sb_dg_os;

	private int totalSet;
	private int totalPlates;

	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity docketBy;

	private Status docketStatus = Status.New;

	/* Functional Detail */

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private StudioEntity studio = new StudioEntity();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private CTPEntity ctp = new CTPEntity();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ChallanEntity challan = new ChallanEntity();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private DeliveryEntity delivery = new DeliveryEntity();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private BillEntity bill = new BillEntity();

	/* Core */

	private JobStatus status = JobStatus.NEW;

	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity lastUpdatedBy;
	private Date lastUpdatedOn = new Date();

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity closedBy;

	/** ------------| constructor |------------ **/

	public JobEntity() {
		this.getStudio().setJob(this);
		this.getCtp().setJob(this);
		this.getDelivery().setJob(this);
		this.getBill().setJob(this);
		this.getChallan().setJob(this);
	}

	/** ------------| business |------------ **/

	/**
	 * This should not be used from anywhere other then JobDao
	 */
	public void populateNo() {
		this.setNo(CommonUtil.nextRegNo().toString());
	}

	/**
	 * This should not be used from anywhere other then JobDao
	 */
	public void processInternal() {
		if (this.getPlates() != null) {
			for (PlateEntity plate : this.getPlates()) {
				this.setFrontBack(this.getFrontBack() + plate.getFrontBack());
				this.setSelfBack(this.getSelfBack() + plate.getSelfBack());
				this.setDoubleGripper(this.getDoubleGripper() + this.getDoubleGripper());
				this.setOneSide(this.getOneSide() + plate.getOneSide());

				this.setTotalSet(this.getTotalSet() + plate.getTheSet());
				this.setTotalPlates(this.getTotalPlates() + plate.getTotal());
			}
		}
		this.populateFb_sb_dg_os();
	}

	/**
	 * This should not be used from anywhere other then processInternal
	 */
	private void populateFb_sb_dg_os() {
		this.setFb_sb_dg_os(this.getFrontBack() + "/" + this.getSelfBack() + "/" + this.getDoubleGripper() + "/"
				+ this.getOneSide());
	}

	/** ------------| setter-getter |------------ **/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Set<PlateEntity> getPlates() {
		return plates;
	}

	public void setPlates(Set<PlateEntity> plates) {
		this.plates = plates;
	}

	public UserEntity getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(UserEntity closedBy) {
		this.closedBy = closedBy;
	}

	public Set<InstructionEntity> getInstructions() {
		return instructions;
	}

	public void setInstructions(Set<InstructionEntity> instructions) {
		this.instructions = instructions;
	}

	/** ------------| override |------------ **/

	/** ------------| business |------------ **/

	public PlateEntity overridePlate(PlateEntity ipPlate) {
		PlateEntity currentPlate = null;
		for (PlateEntity plate : this.getPlates()) {
			if (plate.getId() == ipPlate.getId()) {
				currentPlate = plate;
				break;
			}
		}
		currentPlate.override(ipPlate);
		return currentPlate;
	}

	/**
	 * 1. Fine "instructions" from respective "part"
	 * 2. If ipInst is persisted, override it with persistedInst with ipInst
	 * 3. Else, add ipInst in "instructions"
	 * 4. return respective "inst"
	 */
	public InstructionEntity processInst(InstructionEntity ipInstruction, String part) {
		InstructionEntity currentInstruction = null;
		Set<InstructionEntity> instructions = null;
		if (part.equals("docket")) {
			instructions = this.getInstructions();
		} else if (part.equals("studio")) {
			instructions = this.getStudio().getInstructions();
		} else if (part.equals("delivery")) {
			instructions = this.getDelivery().getInstructions();
		} else if (part.equals("bill")) {
			instructions = this.getBill().getInstructions();
		} else if (part.equals("challan")) {
			instructions = this.getChallan().getInstructions();
		} else if (part.equals("ctp")) {
			instructions = this.getCtp().getInstructions();
		}
		if(ipInstruction.getId() != null) {
			for (InstructionEntity instruction : instructions) {
				if (instruction.getId() == ipInstruction.getId()) {
					currentInstruction = instruction;
					currentInstruction.override(ipInstruction);
					break;
				}
			}			
		}else {
			instructions.add(ipInstruction);
			currentInstruction= ipInstruction;
		}
		return currentInstruction;
	}
}
/*
 * //STUDIO
 * 
 * @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
 * 
 * @JoinTable(name = "JOB_STUDIO_INSTRUCTION", joinColumns = @JoinColumn(name =
 * "jobID"), inverseJoinColumns = @JoinColumn(name = "instructionID")) private
 * Set<InstructionEntity> studioInstructions;
 * 
 * @OneToOne(cascade = CascadeType.ALL) private UserEntity studioBy; private
 * Status studioStatus = Status.New;
 * 
 * //CTP
 * 
 * @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
 * 
 * @JoinTable(name = "JOB_CTP_INSTRUCTION", joinColumns = @JoinColumn(name =
 * "jobID"), inverseJoinColumns = @JoinColumn(name = "instructionID")) private
 * Set<InstructionEntity> ctpInstructions; private UserEntity ctpBy; private
 * Status ctpStatus = Status.New;
 * 
 * //CHALLAN
 * 
 * @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
 * 
 * @JoinTable(name = "JOB_CHALLAN_INSTRUCTION", joinColumns = @JoinColumn(name =
 * "jobID"), inverseJoinColumns = @JoinColumn(name = "instructionID")) private
 * Set<InstructionEntity> challanInstructions; private UserEntity challanBy;
 * private Status challanStatus = Status.New;
 * 
 * //DELIVERY
 * 
 * @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
 * 
 * @JoinTable(name = "JOB_DELIVERY_INSTRUCTION", joinColumns = @JoinColumn(name
 * = "jobID"), inverseJoinColumns = @JoinColumn(name = "instructionID")) private
 * Set<InstructionEntity> deliveryInstructions; private UserEntity deliveryBy;
 * private Status deliveryStatus = Status.New;
 * 
 * //BILL
 * 
 * @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
 * 
 * @JoinTable(name = "JOB__BILL_INSTRUCTION", joinColumns = @JoinColumn(name =
 * "jobID"), inverseJoinColumns = @JoinColumn(name = "instructionID")) private
 * Set<InstructionEntity> billInstructions; private UserEntity billBy; private
 * Status billStatus = Status.New;
 */
