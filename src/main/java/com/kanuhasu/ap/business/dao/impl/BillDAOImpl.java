package com.kanuhasu.ap.business.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.BillEntity;

@Repository
@Transactional
public class BillDAOImpl extends AbstractDAO<BillEntity> {
	@SuppressWarnings("unchecked")
	public List<BillEntity> searchByName(String name) {
		Criteria criteria = super.getSession().createCriteria(BillEntity.class);
		return (List<BillEntity>) criteria.list();
	}
}