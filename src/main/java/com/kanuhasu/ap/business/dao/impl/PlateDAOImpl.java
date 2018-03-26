package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.util.CommonUtil;

@Repository
@Transactional
public class PlateDAOImpl extends AbstractDAO<PlateEntity> {
	@Autowired
	private UserDAOImpl userDao;
	
	@SuppressWarnings("unchecked")
	public List<PlateEntity> searchByName(String name) {
		Criteria criteria = super.getSession().createCriteria(PlateEntity.class);
		return (List<PlateEntity>) criteria.list();
	}
	@Override
	public PlateEntity saveOrUpdate(PlateEntity plate) {
		// loggedInUser
		UserEntity loggedInUser = this.userDao.getByEmailID(CommonUtil.fetchLoginID());
		plate.setLastUpdatedOn(new Date());
		plate.setLastUpdatedBy(loggedInUser);		
		super.saveOrUpdate(plate);
		return plate;		
	}	
} 