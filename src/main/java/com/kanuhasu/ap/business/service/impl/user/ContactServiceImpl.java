package com.kanuhasu.ap.business.service.impl.user;

import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.dao.impl.user.ContactDAOImpl;
import com.kanuhasu.ap.business.service.impl.AbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContactServiceImpl extends AbstractServiceImpl<ContactEntity> {
	
	@Autowired
	private ContactDAOImpl contactDAO;
	
	public ContactEntity save(ContactEntity contact) {
		return contactDAO.save(contact);
	}
	
	public ContactEntity update(ContactEntity contact) {
		return contactDAO.update(contact);
	}
	
	public ContactEntity get(long contactId) {
		return contactDAO.get(contactId, ContactEntity.class);
	}
	
	public List<ContactEntity> list() {
		return contactDAO.list(ContactEntity.class);
	}
	
	public void delete(ContactEntity contact) {
		contactDAO.delete(contact);
	}
	
	public void deletePermanently(ContactEntity contact) {
		contactDAO.deletePermanently(contact);
	}
}