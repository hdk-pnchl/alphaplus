package com.kanuhasu.ap.business.bo.job;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.pojo.Ctp;
import com.kanuhasu.ap.business.pojo.Instruction;

@Entity
@Table(name = "CTP")
public class CTPEntity extends LastUpdateEntity implements Serializable {
	private static final long serialVersionUID = -5509243769426144588L;

	/** ------------| instance |------------ **/

	private Status status = Status.New;
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

	public Ctp pojo() {
		Ctp pojo = new Ctp();
		pojo.setExeByID(exeBy != null ? exeBy.getId() : null);
		pojo.setStatus(status);
		pojo.setId(this.getId());
		pojo.setJobID(this.getJobId());
		return pojo;
	}

	public Ctp pojoFull() {
		Ctp pojo = this.pojo();
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

	public CTPEntity override(Ctp ipCtp) {
		this.setStatus(ipCtp.getStatus());
		return this;
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