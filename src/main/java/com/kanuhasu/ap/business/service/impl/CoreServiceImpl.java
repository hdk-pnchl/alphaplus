package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.cit.auth.CITUserEntity;
import com.kanuhasu.ap.business.dao.impl.CoreDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class CoreServiceImpl extends AbstractServiceImpl<CITUserEntity>{

	@Autowired
	public void setDao(CoreDAOImpl dao) {
		this.dao = dao;
	}
	
	public CITUserEntity saveOrUpdate(CITUserEntity e) {
		return dao.saveOrUpdate(e);
	}
	
	public List<CITUserEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, CITUserEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, CITUserEntity.class);
	}
}