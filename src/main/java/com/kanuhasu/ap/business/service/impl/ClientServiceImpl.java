package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.dao.impl.ClientDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class ClientServiceImpl extends AbstractServiceImpl<ClientEntity> {
	
	@Autowired
	public void setDao(ClientDAOImpl dao) {
		this.dao = dao;
	}
	
	public ClientEntity searchByName(String name) {
		return ((ClientDAOImpl)dao).searchByName(name);
	}
	
	public List<ClientEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, ClientEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, ClientEntity.class);
	}
}