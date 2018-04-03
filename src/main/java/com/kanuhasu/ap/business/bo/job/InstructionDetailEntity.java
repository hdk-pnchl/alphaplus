package com.kanuhasu.ap.business.bo.job;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kanuhasu.ap.business.pojo.Instruction;
import com.kanuhasu.ap.business.util.CommonUtil;

@Entity
@Table(name = "INSTRUCTION_HOLDER")
public class InstructionDetailEntity extends LastUpdateEntity {
	/** ------------| instance |------------ **/

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "INSTRUCTION_DETAIL", joinColumns = @JoinColumn(name = "jobID"), inverseJoinColumns = @JoinColumn(name = "instructionID"))
	private Set<InstructionEntity> instructions = new HashSet<>();

	/** ------------| business |------------ **/

	public InstructionEntity processInst(Instruction instructionPojo) {
		InstructionEntity currentInstruction = null;
		if (CommonUtil.isIdDefined(instructionPojo.getId())) {
			for (InstructionEntity instruction : this.getInstructions()) {
				if (instruction.getId() == instructionPojo.getId()) {
					currentInstruction = instruction;
					break;
				}
			}
		} else {
			currentInstruction = new InstructionEntity();
			this.getInstructions().add(currentInstruction);
		}
		currentInstruction.override(instructionPojo);
		return currentInstruction;
	}

	/** ------------| setter-getter |------------ **/

	public Set<InstructionEntity> getInstructions() {
		return instructions;
	}

	public void setInstructions(Set<InstructionEntity> instructions) {
		this.instructions = instructions;
	}
}