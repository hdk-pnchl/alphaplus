package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.StudioEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.util.CommonUtil;

@Repository
@Transactional
public class StudioDAOImpl extends AbstractDAO<StudioEntity> {
	@Autowired
	private UserDAOImpl userDao;

	@Override
	public StudioEntity merge(StudioEntity plate) {
		// loggedInUser
		UserEntity loggedInUser = this.userDao.getByEmailID(CommonUtil.fetchLoginID());
		plate.setLastUpdatedOn(new Date());
		plate.setLastUpdatedBy(loggedInUser);
		super.saveOrUpdate(plate);
		return plate;
	}
}