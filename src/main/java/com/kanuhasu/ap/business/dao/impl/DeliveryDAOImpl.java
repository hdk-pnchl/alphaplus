package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.DeliveryEntity;
import com.kanuhasu.ap.business.util.AuthUtil;

@Repository
@Transactional
public class DeliveryDAOImpl extends AbstractDAO<DeliveryEntity> {
	@Override
	public DeliveryEntity merge(DeliveryEntity entity) {
		entity.setLastUpdatedOn(new Date());
		entity.setLastUpdatedById(AuthUtil.fetchLoggedInUserID());
		super.saveOrUpdate(entity);
		return entity;
	}
}