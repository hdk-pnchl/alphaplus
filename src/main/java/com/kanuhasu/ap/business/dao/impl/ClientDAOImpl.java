package com.kanuhasu.ap.business.dao.impl;

import java.text.ParseException;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.util.SearchInput;

@Repository
@Transactional
public class ClientDAOImpl extends AbstractDAO {
	
	public ClientEntity save(ClientEntity client) {
		this.getSession().save(client);
		return client;
	}
	
	public ClientEntity update(ClientEntity client) {
		this.getSession().update(client);
		return client;
	}
	
	public ClientEntity saveOrUpdate(ClientEntity client) {
		this.getSession().saveOrUpdate(client);
		return client;
	}
	
	public ClientEntity get(long clientID) {
		ClientEntity client = null;
		Object clientObject = this.getSession().get(ClientEntity.class, clientID);
		if(clientObject != null) {
			client = (ClientEntity) clientObject;
		}
		return client;
	}
	
	@SuppressWarnings("unchecked")
	public List<ClientEntity> list() {
		Criteria criteria = getSession().createCriteria(ClientEntity.class);
		return (List<ClientEntity>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ClientEntity> searchByName(String name) {
		Criteria criteria = getSession().createCriteria(ClientEntity.class);
		return (List<ClientEntity>) criteria.list();		
	}

	@SuppressWarnings("unchecked")
	public List<ClientEntity> search(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(ClientEntity.class);
		super.search(searchInput, criteria);
		return criteria.list();
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(ClientEntity.class);
		super.getTotalRowCount(searchInput, criteria);
		Long rowCount = (Long) criteria.uniqueResult();
		return rowCount;
	}
	
	public void delete(ClientEntity client) {
		// TODO Auto-generated method stub
	}
	
	public void deletePermanently(ClientEntity client) {
		this.getSession().delete(client);
	}
}