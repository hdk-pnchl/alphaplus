package com.kanuhasu.ap.business.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.PlateEntity;

@Repository
@Transactional
public class PlateDAOImpl extends AbstractDAO<PlateEntity> {
	public PlateEntity update(PlateEntity entity) {
		entity = super.save(entity);
		this.initLazyProp(entity);
		return entity;
	}

	public PlateEntity fetchByID(long id) {
		return super.fetchByID(id, PlateEntity.class);
	}

	private void initLazyProp(PlateEntity plate) {
	}
}