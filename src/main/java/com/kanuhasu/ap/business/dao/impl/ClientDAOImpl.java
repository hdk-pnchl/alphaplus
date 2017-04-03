package com.kanuhasu.ap.business.dao.impl;

import java.text.ParseException;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.dao.impl.user.AddressDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.ContactDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Repository
@Transactional
public class ClientDAOImpl extends AbstractDAO {
	
	@Autowired
	private AddressDAOImpl addressDAOImpl;
	@Autowired
	private ContactDAOImpl contactDAOImpl;
	
	public ClientEntity saveOrUpdate(ClientEntity client) {
		if(client.getAddressDetail()!=null){
			for(Entry<String, AddressEntity>  addressEntry: client.getAddressDetail().entrySet()){
				AddressEntity address= addressEntry.getValue();
				if(address!=null){
					addressDAOImpl.saveOrUpdate(address);	
				}
			}			
		}
		if(client.getContactDetail()!=null){
			for(Entry<String, ContactEntity>  addressEntry: client.getContactDetail().entrySet()){
				ContactEntity contact= addressEntry.getValue();
				if(contact!=null){
					contactDAOImpl.saveOrUpdate(contact);	
				}
			}			
		}		
		this.getSession().saveOrUpdate(client);
		return client;
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
}