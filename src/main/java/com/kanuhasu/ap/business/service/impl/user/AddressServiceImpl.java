package com.kanuhasu.ap.business.service.impl.user;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.dao.impl.user.AddressDAOImpl;
import com.kanuhasu.ap.business.service.impl.AbstractServiceImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class AddressServiceImpl extends AbstractServiceImpl<AddressEntity> {
	
	@Autowired
	public void setDao(AddressDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<AddressEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, AddressEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, AddressEntity.class);
	}
}