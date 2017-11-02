package com.kanuhasu.ap.business.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.DeliveryEntity;

@Repository
@Transactional
public class DeliveryDAOImpl extends AbstractDAO<DeliveryEntity> {
	@SuppressWarnings("unchecked")
	public List<DeliveryEntity> searchByName(String name) {
		Criteria criteria = super.getSession().createCriteria(DeliveryEntity.class);
		return (List<DeliveryEntity>) criteria.list();
	}
}