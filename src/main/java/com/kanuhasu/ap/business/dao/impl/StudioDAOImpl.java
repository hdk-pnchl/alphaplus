package com.kanuhasu.ap.business.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.StudioEntity;

@Repository
@Transactional
public class StudioDAOImpl extends AbstractDAO<StudioEntity> {
	@SuppressWarnings("unchecked")
	public List<StudioEntity> searchByName(String name) {
		Criteria criteria = super.getSession().createCriteria(StudioEntity.class);
		return (List<StudioEntity>) criteria.list();
	}
} 