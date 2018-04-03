package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.InstructionEntity;
import com.kanuhasu.ap.business.util.AuthUtil;

@Repository
@Transactional
public class InstDAOImpl extends AbstractDAO<InstructionEntity> {
	@Override
	public InstructionEntity saveOrUpdate(InstructionEntity entity) {
		entity.setLastUpdatedOn(new Date());
		entity.setLastUpdatedById(AuthUtil.fetchLoggedInUserID());
		super.saveOrUpdate(entity);
		return entity;
	}
}