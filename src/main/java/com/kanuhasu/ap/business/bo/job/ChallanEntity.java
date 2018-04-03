package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.pojo.Challan;
import com.kanuhasu.ap.business.pojo.Instruction;
import com.kanuhasu.ap.business.util.CommonUtil;

@Entity
@Table(name = "Challan")
public class ChallanEntity extends LastUpdateEntity implements Serializable {
	private static final long serialVersionUID = 7407199838414255161L;

	/** ------------| instance |------------ **/

	private Status status = Status.New;
	private String no;
	private Date date = new Date();
	// exeBy
	@Transient
	private UserEntity exeBy;
	private long exeById;
	// instructions
	@Transient
	private InstructionDetailEntity instructionDetail = new InstructionDetailEntity();
	private long instructionDetailId;
	// job
	private long jobId;

	/** ------------| business |------------ **/

	public Challan pojo() {
		Challan pojo = new Challan();
		pojo.setExeByID(exeBy != null ? exeBy.getId() : null);
		pojo.setStatus(status);
		pojo.setId(id);
		pojo.setJobID(this.getJobId());
		pojo.setDate(date);
		return pojo;
	}

	public Challan pojoFull() {
		Challan pojo = this.pojo();
		pojo.setLastUpdatedOn(this.getLastUpdatedOn());
		Set<Instruction> instructions = pojo.getInstructions();
		if (this.getInstructionDetail() != null) {
			for (InstructionEntity instruction : this.getInstructionDetail().getInstructions()) {
				instructions.add(instruction.pojo());
			}
		}
		if (this.getExeBy() != null) {
			pojo.setExeBy(this.getExeBy().pojo());
		}
		return pojo;
	}

	/**
	 * This should not be used from anywhere other then JobDao
	 */
	public void populateChallanNo() {
		this.setNo(CommonUtil.nextRegNo().toString());
	}

	public ChallanEntity override(Challan pojo) {
		this.setStatus(pojo.getStatus());
		this.setDate(pojo.getDate());
		return this;
	}

	/** ------------| setter-getter |------------ **/

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public UserEntity getExeBy() {
		return exeBy;
	}

	public void setExeBy(UserEntity exeBy) {
		this.exeBy = exeBy;
	}

	public long getExeById() {
		return exeById;
	}

	public void setExeById(long exeById) {
		this.exeById = exeById;
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

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}