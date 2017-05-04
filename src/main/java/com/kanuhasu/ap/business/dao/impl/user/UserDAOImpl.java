package com.kanuhasu.ap.business.dao.impl.user;

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
import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class UserDAOImpl extends AbstractDAO<UserEntity> {
	
	@Autowired
	private AddressDAOImpl addressDAOImpl;
	@Autowired
	private ContactDAOImpl contactDAOImpl;
	@Autowired
	private IDDetailDAOImpl idDetailDAOImpl;
				
	public boolean makeItAdmin(String emailID, RoleEntity adminRole) {
		UserEntity user;
		Object userObj = this.getByEmailID(emailID);
		if(userObj != null) {
			user = (UserEntity) userObj;
			user.getRoles().add(adminRole);
			this.update(user);
			return true;
		}
		return false;
	}
	
	public UserEntity getByEmailID(String emailID) {
		UserEntity user = null;
		Criteria criteria = super.getSession().createCriteria(UserEntity.class);
		if(emailID != null) {
			criteria.add(Restrictions.eq("emailID", emailID));
		}
		@SuppressWarnings("unchecked")	
		List<UserEntity> list = criteria.list();
		if(list != null && !list.isEmpty()) {
			user = list.get(0);
		}
		return user;
	}
	
	@Override
	public UserEntity saveOrUpdate(UserEntity client) {
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
		if(client.getIdDetail()!=null){
			idDetailDAOImpl.saveOrUpdate(client.getIdDetail());
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
	
	public UserEntity get(long id) {
		return super.get(id, UserEntity.class);
	}
}