package com.kanuhasu.ap.business.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.cit.auth.CITUserEntity;

@Repository
@Transactional
public class CoreDAOImpl extends AbstractDAO<CITUserEntity> {

	
	@Override
	public CITUserEntity saveOrUpdate(CITUserEntity cituser) {
		//super.getSession().saveOrUpdate(cituser);
		super.saveOrUpdate(cituser);
		return cituser;
	}
}