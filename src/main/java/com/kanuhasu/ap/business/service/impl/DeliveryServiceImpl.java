package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.DeliveryEntity;
import com.kanuhasu.ap.business.dao.impl.DeliveryDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class DeliveryServiceImpl extends AbstractServiceImpl<DeliveryEntity> {

	@Autowired
	public void setDao(DeliveryDAOImpl dao) {
		this.dao = dao;
	}

	public List<DeliveryEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, DeliveryEntity.class);
	}

	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, DeliveryEntity.class);
	}
}