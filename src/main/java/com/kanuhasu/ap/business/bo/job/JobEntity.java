package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.pojo.Docket;
import com.kanuhasu.ap.business.pojo.Instruction;
import com.kanuhasu.ap.business.pojo.Job;
import com.kanuhasu.ap.business.pojo.Plate;
import com.kanuhasu.ap.business.type.bo.user.BindingStyle;
import com.kanuhasu.ap.business.type.bo.user.ColorCopySize;
import com.kanuhasu.ap.business.util.CommonUtil;

@Entity(name = "Job")
public class JobEntity extends LastUpdateEntity implements Serializable {
	private static final long serialVersionUID = 5696903605244349608L;

	/** ------------| instance |------------ **/

	private String name;
	// pc-ap
	private String no;

	private Date receivedDate = new Date();
	private Date receivedTime = new Date();

	private Date targetDate = new Date();
	private Date targetTime = new Date();

	// client
	@Transient
	private ClientEntity client;
	private long clientId;

	/* Instructions */
	@Transient
	private InstructionDetailEntity instructionDetail;
	private long instructionDetailId;

	/* Plate Detail */
	private float cut = 1;
	private float open = 1;
	private int page = 1;
	// center-pinning, perfect-binding, section-binding, Wiro-binding
	private BindingStyle bindingStyle = BindingStyle.CENTER;
	// Color copy size: A3/A4. Example: 2 of A3.
	private ColorCopySize colorCopySize = ColorCopySize.A4;
	/* Internal */
	@Transient
	private PlateDetailEntity plateDetail = new PlateDetailEntity();
	private long plateDetailID;
	// total should be "F/B + S/B + D/G + OS" or less
	private int frontBack;
	private int selfBack;
	private int doubleGripper;
	private int oneSide;
	private String fb_sb_dg_os;
	// total
	private int totalSet;
	private int totalPlates;

	// docket
	@Transient
	private UserEntity docketBy;
	private long docketById;

	private Status docketStatus = Status.New;

	/* Functional Detail */

	@Transient
	private StudioEntity studio = new StudioEntity();
	private long studioId;

	@Transient
	private CTPEntity ctp = new CTPEntity();
	private long ctpId;

	@Transient
	private ChallanEntity challan = new ChallanEntity();
	private long challanId;

	@Transient
	private DeliveryEntity delivery = new DeliveryEntity();
	private long deliveryId;

	@Transient
	private BillEntity bill = new BillEntity();
	private long billId;

	private JobStatus status = JobStatus.NEW;

	@Transient
	private UserEntity closedBy;
	private long closedById;

	/** ------------| constructor |------------ **/

	public JobEntity() {
	}

	/** ------------| business |------------ **/

	public Job pojo() {
		Job pojo = new Job();
		pojo.setId(this.getId());
		pojo.setName(name);
		pojo.setNo(no);
		pojo.setReceivedDate(receivedDate);
		pojo.setTargetDate(targetDate);
		// pojo.setClient(client.pojo());
		pojo.setCut(cut);
		pojo.setOpen(open);
		pojo.setPage(page);
		pojo.setBindingStyle(bindingStyle);
		pojo.setColorCopySize(colorCopySize);
		pojo.setFrontBack(frontBack);
		pojo.setSelfBack(selfBack);
		pojo.setDoubleGripper(doubleGripper);
		pojo.setOneSide(oneSide);
		pojo.setFb_sb_dg_os(fb_sb_dg_os);
		pojo.setTotalSet(totalSet);
		pojo.setTotalPlates(totalPlates);
		pojo.setDocketStatus(docketStatus);
		pojo.setStatus(status);
		pojo.setClientId(clientId);
		return pojo;
	}

	public Job pojoFull() {
		Job pojo = this.pojo();
		// instructions
		Set<Instruction> instructions = pojo.getInstructions();
		if (this.getInstructionDetail() != null) {
			for (InstructionEntity instruction : this.getInstructionDetail().getInstructions()) {
				instructions.add(instruction.pojo());
			}
		}
		// plates
		Set<Plate> plates = pojo.getPlates();
		if (this.getPlateDetail() != null) {
			for (PlateEntity plate : this.getPlateDetail().getPlates()) {
				plates.add(plate.pojo());
			}
		}
		// internal
		if (this.getStudio() != null) {
			pojo.setStudio(this.getStudio().pojoFull());
		}
		if (this.getCtp() != null) {
			pojo.setCtp(this.getCtp().pojoFull());
		}
		if (this.getChallan() != null) {
			pojo.setChallan(this.getChallan().pojoFull());
		}
		if (this.getDelivery() != null) {
			pojo.setDelivery(this.getDelivery().pojoFull());
		}
		if (this.getBill() != null) {
			pojo.setBill(this.getBill().pojoFull());
		}
		// client
		if (this.getClient() != null) {
			pojo.setClient(this.getClient().pojoFull());
		}
		// docket
		if (this.getDocketBy() != null) {
			pojo.setDocketBy(this.getDocketBy().pojoFull());
		}
		return pojo;
	}

	public void overrideDocket(Docket docket) {
		this.setName(docket.getName());
		this.setNo(docket.getNo());
		this.setReceivedDate(docket.getReceivedDate());
		this.setTargetDate(docket.getTargetDate());
		this.setCut(docket.getCut());
		this.setOpen(docket.getOpen());
		this.setPage(docket.getPage());
		this.setBindingStyle(docket.getBindingStyle());
		this.setColorCopySize(docket.getColorCopySize());
		this.setDocketStatus(docket.getDocketStatus());
	}

	/**
	 * This should not be used from anywhere other then JobDao
	 */
	public void populateNo() {
		this.setNo(CommonUtil.nextRegNo().toString());
	}

	/**
	 * FrontBack, SelfBack, DoubleGripper, OneSide, TotalSet, TotalPlates This
	 * should not be used from anywhere other then JobDao
	 */
	public void processInternal() {
		if (this.getPlateDetail() != null) {
			for (PlateEntity plate : this.getPlateDetail().getPlates()) {
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

	public boolean isDocketByChanged(long pojoDocketById) {
		boolean isDocketByChanged = false;
		if (this.getDocketBy() == null) {
			isDocketByChanged = true;
		} else if (this.getDocketBy().getId() != pojoDocketById) {
			isDocketByChanged = true;
		}
		return isDocketByChanged;
	}

	public boolean isClientChanged(long pojoClientId) {
		boolean isClientChanged = false;
		if (this.getClient() == null) {
			isClientChanged = true;
		} else if (this.getClient().getId() != pojoClientId) {
			isClientChanged = true;
		}
		return isClientChanged;
	}

	public boolean isExeByChanged(UserEntity currentExeBy, long exeById) {
		boolean isExeByChanged = false;
		if (currentExeBy == null) {
			isExeByChanged = true;
		} else if (currentExeBy.getId() != exeById) {
			isExeByChanged = true;
		}
		return isExeByChanged;
	}

	/** ------------| override |------------ **/

	/** ------------| setter-getter |------------ **/

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

	public Status getDocketStatus() {
		return docketStatus;
	}

	public void setDocketStatus(Status docketStatus) {
		this.docketStatus = docketStatus;
	}

	public UserEntity getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(UserEntity closedBy) {
		this.closedBy = closedBy;
	}

	public PlateDetailEntity getPlateDetail() {
		return plateDetail;
	}

	public void setPlateDetail(PlateDetailEntity plateDetail) {
		this.plateDetail = plateDetail;
	}

	public long getPlateDetailID() {
		return plateDetailID;
	}

	public void setPlateDetailID(long plateDetailID) {
		this.plateDetailID = plateDetailID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public InstructionDetailEntity getInstructionDetail() {
		return instructionDetail;
	}

	public void setInstructionDetail(InstructionDetailEntity instructionDetail) {
		this.instructionDetail = instructionDetail;
	}

	public long getInstructionDetailId() {
		return instructionDetailId;
	}

	public void setInstructionDetailId(long instructionDetailId) {
		this.instructionDetailId = instructionDetailId;
	}

	public long getDocketById() {
		return docketById;
	}

	public void setDocketById(long docketById) {
		this.docketById = docketById;
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
}