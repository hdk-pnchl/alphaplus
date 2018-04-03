package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.ChallanEntity;
import com.kanuhasu.ap.business.util.AuthUtil;

@Repository
@Transactional
public class ChallanDAOImpl extends AbstractDAO<ChallanEntity> {
	@Override
	public ChallanEntity merge(ChallanEntity entity) {
		entity.setLastUpdatedOn(new Date());
		entity.setLastUpdatedById(AuthUtil.fetchLoggedInUserID());
		super.saveOrUpdate(entity);
		return entity;
	}
}