package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.dao.impl.PlateDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class PlateServiceImpl extends AbstractServiceImpl<PlateEntity> {
	
	@Autowired
	public void setDao(PlateDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<PlateEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, PlateEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, PlateEntity.class);
	}
}