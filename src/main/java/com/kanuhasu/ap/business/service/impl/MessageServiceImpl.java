package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.MessageEntity;
import com.kanuhasu.ap.business.dao.impl.MessageDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class MessageServiceImpl extends AbstractServiceImpl<MessageEntity>{

	@Autowired
	public void setDao(MessageDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<MessageEntity> listByEmailID(String emailId) {
		return ((MessageDAOImpl)dao).listByEmailID(emailId);
	}
	
	public List<MessageEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, MessageEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, MessageEntity.class);
	}
}