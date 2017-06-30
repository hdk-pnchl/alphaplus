package com.kanuhasu.ap.business.dao.impl.user;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class ContactDAOImpl extends AbstractDAO<ContactEntity> {
	@Override
	public ContactEntity saveOrUpdate(ContactEntity contact) {
		contact.setContactStr(contact.toString());
		return super.saveOrUpdate(contact);
	}	
}