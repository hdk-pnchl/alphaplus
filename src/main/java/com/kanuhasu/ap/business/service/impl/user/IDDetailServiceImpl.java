package com.kanuhasu.ap.business.service.impl.user;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.IDDetailEntity;
import com.kanuhasu.ap.business.dao.impl.user.IDDetailDAOImpl;
import com.kanuhasu.ap.business.service.impl.AbstractServiceImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class IDDetailServiceImpl extends AbstractServiceImpl<IDDetailEntity> {
	
	@Autowired
	public void setDao(IDDetailDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<IDDetailEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, IDDetailEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, IDDetailEntity.class);
	}
}