package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.StudioEntity;
import com.kanuhasu.ap.business.util.AuthUtil;

@Repository
@Transactional
public class StudioDAOImpl extends AbstractDAO<StudioEntity> {
	@Override
	public StudioEntity merge(StudioEntity plate) {
		plate.setLastUpdatedOn(new Date());
		plate.setLastUpdatedById(AuthUtil.fetchLoggedInUser().getId());
		super.saveOrUpdate(plate);
		return plate;
	}
}