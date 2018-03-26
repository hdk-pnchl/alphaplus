package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.ChallanEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.util.CommonUtil;

@Repository
@Transactional
public class ChallanDAOImpl extends AbstractDAO<ChallanEntity> {
	@Autowired
	private UserDAOImpl userDao;

	@Override
	public ChallanEntity merge(ChallanEntity plate) {
		// loggedInUser
		UserEntity loggedInUser = this.userDao.getByEmailID(CommonUtil.fetchLoginID());
		plate.setLastUpdatedOn(new Date());
		plate.setLastUpdatedBy(loggedInUser);
		super.saveOrUpdate(plate);
		return plate;
	}
}