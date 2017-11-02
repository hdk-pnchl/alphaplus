package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.CTPEntity;
import com.kanuhasu.ap.business.dao.impl.CTPDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class CTPServiceImpl extends AbstractServiceImpl<CTPEntity> {
	
	@Autowired
	public void setDao(CTPDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<CTPEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, CTPEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, CTPEntity.class);
	}
}