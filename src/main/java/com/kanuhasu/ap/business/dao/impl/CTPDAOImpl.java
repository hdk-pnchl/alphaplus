package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.CTPEntity;
import com.kanuhasu.ap.business.util.AuthUtil;

@Repository
@Transactional
public class CTPDAOImpl extends AbstractDAO<CTPEntity> {
	@Override
	public CTPEntity merge(CTPEntity plate) {
		plate.setLastUpdatedOn(new Date());
		plate.setLastUpdatedById(AuthUtil.fetchLoggedInUser().getId());
		super.saveOrUpdate(plate);
		return plate;
	}
}