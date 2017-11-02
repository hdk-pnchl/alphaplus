package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.ChallanEntity;
import com.kanuhasu.ap.business.dao.impl.ChallanDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class ChallanServiceImpl extends AbstractServiceImpl<ChallanEntity> {
	
	@Autowired
	public void setDao(ChallanDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<ChallanEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, ChallanEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, ChallanEntity.class);
	}
}