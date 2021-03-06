package com.kanuhasu.ap.business.service.impl.user;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.dao.impl.user.ContactDAOImpl;
import com.kanuhasu.ap.business.service.impl.AbstractServiceImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class ContactServiceImpl extends AbstractServiceImpl<ContactEntity> {
	
	@Autowired
	public void setDao(ContactDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<ContactEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, ContactEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, ContactEntity.class);
	}
}