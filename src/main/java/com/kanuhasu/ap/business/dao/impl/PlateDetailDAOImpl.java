package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.PlateDetailEntity;
import com.kanuhasu.ap.business.util.AuthUtil;

@Repository
@Transactional
public class PlateDetailDAOImpl extends AbstractDAO<PlateDetailEntity> {
	@Override
	public PlateDetailEntity merge(PlateDetailEntity plateDetail) {
		plateDetail.setLastUpdatedOn(new Date());
		plateDetail.setLastUpdatedById(AuthUtil.fetchLoggedInUser().getId());
		super.saveOrUpdate(plateDetail);
		return plateDetail;
	}
}