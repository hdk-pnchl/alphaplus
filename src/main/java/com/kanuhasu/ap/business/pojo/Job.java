package com.kanuhasu.ap.business.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.bo.job.JobStatus;
import com.kanuhasu.ap.business.bo.job.Status;
import com.kanuhasu.ap.business.type.bo.user.BindingStyle;
import com.kanuhasu.ap.business.type.bo.user.ColorCopySize;

public class Job {
	/** ------------| instance |------------ **/

	private Long id;
	private String name;
	private String no;
	private Date receivedDate = new Date();
	private Date targetDate = new Date();
	private Set<Instruction> instructions = new HashSet<>();

	private float cut = 1;
	private float open = 1;
	private int page = 1;
	private BindingStyle bindingStyle = BindingStyle.CENTER;
	private ColorCopySize colorCopySize = ColorCopySize.A4;

	private Set<Plate> plates = new HashSet<>();

	private int frontBack;
	private int selfBack;
	private int doubleGripper;
	private int oneSide;
	private String fb_sb_dg_os;

	private int totalSet;
	private int totalPlates;

	private Status docketStatus = Status.New;
	private JobStatus status = JobStatus.NEW;

	private Studio studio = new Studio();
	private long studioId;
	private Ctp ctp = new Ctp();
	private long ctpId;
	private Challan challan = new Challan();
	private long challanId;
	private Delivery delivery = new Delivery();
	private long deliveryId;
	private Bill bill = new Bill();
	private long billId;

	private User closedBy;
	private long closedById;

	private User docketBy;
	private long docketById;

	private Client client;
	private long clientId;
	private String clientName;

	/** ------------| business |------------ **/

	public JobEntity entity() {
		JobEntity entity = new JobEntity();
		return entity;
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

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Set<Instruction> getInstructions() {
		return instructions;
	}

	public void setInstructions(Set<Instruction> instructions) {
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

	public Set<Plate> getPlates() {
		return plates;
	}

	public void setPlates(Set<Plate> plates) {
		this.plates = plates;
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

	public Status getDocketStatus() {
		return docketStatus;
	}

	public void setDocketStatus(Status docketStatus) {
		this.docketStatus = docketStatus;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public Studio getStudio() {
		return studio;
	}

	public void setStudio(Studio studio) {
		this.studio = studio;
	}

	public Ctp getCtp() {
		return ctp;
	}

	public void setCtp(Ctp ctp) {
		this.ctp = ctp;
	}

	public Challan getChallan() {
		return challan;
	}

	public void setChallan(Challan challan) {
		this.challan = challan;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public User getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(User closedBy) {
		this.closedBy = closedBy;
	}

	public User getDocketBy() {
		return docketBy;
	}

	public void setDocketBy(User docketBy) {
		this.docketBy = docketBy;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public long getStudioId() {
		return studioId;
	}

	public void setStudioId(long studioId) {
		this.studioId = studioId;
	}

	public long getCtpId() {
		return ctpId;
	}

	public void setCtpId(long ctpId) {
		this.ctpId = ctpId;
	}

	public long getChallanId() {
		return challanId;
	}

	public void setChallanId(long challanId) {
		this.challanId = challanId;
	}

	public long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public long getBillId() {
		return billId;
	}

	public void setBillId(long billId) {
		this.billId = billId;
	}

	public long getClosedById() {
		return closedById;
	}

	public void setClosedById(long closedById) {
		this.closedById = closedById;
	}

	public long getDocketById() {
		return docketById;
	}

	public void setDocketById(long docketById) {
		this.docketById = docketById;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
}