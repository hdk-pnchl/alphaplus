package com.kanuhasu.ap.business.dao.impl;

import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.dao.impl.user.AddressDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.ContactDAOImpl;

@Repository
@Transactional
public class ClientDAOImpl extends AbstractDAO<ClientEntity> {
	
	@Autowired
	private AddressDAOImpl addressDAOImpl;
	@Autowired
	private ContactDAOImpl contactDAOImpl;
	
	@Override
	public ClientEntity saveOrUpdate(ClientEntity client) {
		if(client.getAddressDetail()!=null){
			for(Entry<String, AddressEntity> addressEntry: client.getAddressDetail().entrySet()){
				AddressEntity address= addressEntry.getValue();
				if(address!=null){
					addressDAOImpl.saveOrUpdate(address);	
				}
			}			
		}
		if(client.getContactDetail()!=null){
			for(Entry<String, ContactEntity> contactEntry: client.getContactDetail().entrySet()){
				ContactEntity contact= contactEntry.getValue();
				if(contact!=null){
					contactDAOImpl.saveOrUpdate(contact);	
				}
			}			
		}		
		super.saveOrUpdate(client);
		return client;
	}
	
	@SuppressWarnings("unchecked")
	public ClientEntity searchByName(String name) {
		ClientEntity client = null;
		Criteria criteria = this.getSession().createCriteria(ClientEntity.class);
		criteria.add(Restrictions.eq("name", name));
		List<ClientEntity> clients= criteria.list();
		if(!clients.isEmpty()){
			client= clients.get(0);
		}
		return client;		
	}
}