package com.kanuhasu.ap.business.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.CTPEntity;

@Repository
@Transactional
public class CTPDAOImpl extends AbstractDAO<CTPEntity> {
	@SuppressWarnings("unchecked")
	public List<CTPEntity> searchByName(String name) {
		Criteria criteria = super.getSession().createCriteria(CTPEntity.class);
		return (List<CTPEntity>) criteria.list();
	}
}