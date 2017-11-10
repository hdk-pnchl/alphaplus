package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;
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
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.AddressDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.ContactDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.util.CommonUtil;

@Repository
@Transactional
public class ClientDAOImpl extends AbstractDAO<ClientEntity> {
	
	@Autowired
	private AddressDAOImpl addressDAOImpl;
	@Autowired
	private ContactDAOImpl contactDAOImpl;
	@Autowired
	private UserDAOImpl userDao; 
	
	@Override
	public ClientEntity saveOrUpdate(ClientEntity client) {
		UserEntity loggedInUser= userDao.getByEmailID(CommonUtil.fetchLoginID());
		
		client.setLastUpdatedBy(loggedInUser);
		client.setLastUpdatedOn(new Date());
		
		if(client.getAddressDetail()!=null){
			for(Entry<String, AddressEntity> addressEntry: client.getAddressDetail().entrySet()){
				AddressEntity address= addressEntry.getValue();
				if(address!=null){
					address.setLastUpdatedBy(loggedInUser);
					address.setLastUpdatedOn(new Date());
					addressDAOImpl.saveOrUpdate(address);	
				}
			}			
		}
		if(client.getContactDetail()!=null){
			for(Entry<String, ContactEntity> contactEntry: client.getContactDetail().entrySet()){
				ContactEntity contact= contactEntry.getValue();
				if(contact!=null){
					contact.setLastUpdatedBy(loggedInUser);
					contact.setLastUpdatedOn(new Date());					
					contactDAOImpl.saveOrUpdate(contact);	
				}
			}			
		}		
		super.saveOrUpdate(client);
		return client;
	}
	
	public ClientEntity getByEmailID(String emailID) {
		ClientEntity client = null;
		Criteria criteria = super.getSession().createCriteria(ClientEntity.class);
		if(emailID != null) {
			criteria.add(Restrictions.eq("emailID", emailID));
		}
		@SuppressWarnings("unchecked")	
		List<ClientEntity> list = criteria.list();
		if(list != null && !list.isEmpty()) {
			client = list.get(0);
		}
		return client;
	}
	
	@SuppressWarnings("unchecked")
	public List<ClientEntity> getAllByName(String name) {
		Criteria criteria = this.getSession().createCriteria(ClientEntity.class);
		criteria.add(Restrictions.like("name", "%" + name + "%"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<ClientEntity> clients= criteria.list();
		return clients;		
	}
	
	@SuppressWarnings("unchecked")
	public ClientEntity getByName(String name) {
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