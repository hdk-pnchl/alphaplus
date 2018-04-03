package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.pojo.Bill;
import com.kanuhasu.ap.business.pojo.Instruction;

@Entity
@Table(name = "Bill")
public class BillEntity extends LastUpdateEntity implements Serializable {
	private static final long serialVersionUID = 6731587709950299442L;

	/** ------------| instance |------------ **/

	private Status status = Status.New;
	private Date date = new Date();
	private Date time = new Date();

	// exeBy
	@Transient
	private UserEntity exeBy;
	private long exeById;

	// instructions
	@Transient
	private InstructionDetailEntity instructionDetail= new InstructionDetailEntity();
	private long instructionDetailId;
	// job
	private long jobId;

	/** ------------| business |------------ **/

	public BillEntity override(Bill ipBill) {
		this.setStatus(ipBill.getStatus());
		this.setDate(ipBill.getDate());
		return this;
	}

	public Bill pojo() {
		Bill pojo = new Bill();
		pojo.setExeByID(exeBy != null ? exeBy.getId() : null);
		pojo.setStatus(status);
		pojo.setId(id);
		pojo.setJobID(this.getJobId());
		pojo.setDate(date);
		return pojo;
	}

	public Bill pojoFull() {
		Bill pojo = this.pojo();
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

	/** ------------| setter-getter |------------ **/

	public UserEntity getExeBy() {
		return exeBy;
	}

	public void setExeBy(UserEntity exeBy) {
		this.exeBy = exeBy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
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