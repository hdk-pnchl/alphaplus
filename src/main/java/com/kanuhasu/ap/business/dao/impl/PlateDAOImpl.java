package com.kanuhasu.ap.business.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.PlateEntity;

@Repository
@Transactional
public class PlateDAOImpl extends AbstractDAO<PlateEntity> {
	@SuppressWarnings("unchecked")
	public List<PlateEntity> searchByName(String name) {
		Criteria criteria = super.getSession().createCriteria(PlateEntity.class);
		return (List<PlateEntity>) criteria.list();
	}
} 