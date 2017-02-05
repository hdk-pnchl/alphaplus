package com.kanuhasu.ap.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.dao.impl.ClientDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class ClientServiceImpl {
	
	@Autowired
	private ClientDAOImpl clientDAO;
	
	public ClientEntity save(ClientEntity client) {
		return clientDAO.save(client);
	}
	
	public ClientEntity update(ClientEntity client) {
		return clientDAO.update(client);
	}
	
	public ClientEntity saveOrUpdate(ClientEntity client) {
		return clientDAO.saveOrUpdate(client);
	}
	
	public ClientEntity get(long clientId) {
		return clientDAO.get(clientId);
	}
	
	public List<ClientEntity> list() {
		return clientDAO.list();
	}
	
	public List<ClientEntity> search(SearchInput searchInput) {
		return clientDAO.search(searchInput);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) {
		return clientDAO.getTotalRowCount(searchInput);
	}
	
	public void delete(ClientEntity client) {
		clientDAO.delete(client);
	}
	
	public void deletePermanently(ClientEntity client) {
		clientDAO.deletePermanently(client);
	}
}