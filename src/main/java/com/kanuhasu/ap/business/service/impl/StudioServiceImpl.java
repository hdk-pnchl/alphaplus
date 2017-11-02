package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.StudioEntity;
import com.kanuhasu.ap.business.dao.impl.StudioDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class StudioServiceImpl extends AbstractServiceImpl<StudioEntity> {
	
	@Autowired
	public void setDao(StudioDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<StudioEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, StudioEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, StudioEntity.class);
	}
}