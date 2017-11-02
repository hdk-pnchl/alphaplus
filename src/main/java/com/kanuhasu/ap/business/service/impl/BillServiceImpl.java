package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.BillEntity;
import com.kanuhasu.ap.business.dao.impl.BillDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class BillServiceImpl extends AbstractServiceImpl<BillEntity> {
	
	@Autowired
	public void setDao(BillDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<BillEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, BillEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, BillEntity.class);
	}
}