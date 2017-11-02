package com.kanuhasu.ap.business.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.ChallanEntity;

@Repository
@Transactional
public class ChallanDAOImpl extends AbstractDAO<ChallanEntity> {
	@SuppressWarnings("unchecked")
	public List<ChallanEntity> searchByName(String name) {
		Criteria criteria = super.getSession().createCriteria(ChallanEntity.class);
		return (List<ChallanEntity>) criteria.list();
	}
} 