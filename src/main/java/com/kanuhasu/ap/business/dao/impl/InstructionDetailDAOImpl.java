package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.InstructionDetailEntity;
import com.kanuhasu.ap.business.util.AuthUtil;

@Repository
@Transactional
public class InstructionDetailDAOImpl extends AbstractDAO<InstructionDetailEntity> {
	@Override
	public InstructionDetailEntity merge(InstructionDetailEntity instructionDetail) {
		instructionDetail.setLastUpdatedOn(new Date());
		instructionDetail.setLastUpdatedById(AuthUtil.fetchLoggedInUser().getId());
		super.saveOrUpdate(instructionDetail);
		return instructionDetail;
	}

	public InstructionDetailEntity fetchByID(long id) {
		return super.fetchByID(id, InstructionDetailEntity.class);
	}

}