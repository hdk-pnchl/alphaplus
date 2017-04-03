package com.kanuhasu.ap.business.service.impl.user;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.dao.impl.user.ContactDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class ContactServiceImpl {
	
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
	
	public List<ContactEntity> search(SearchInput searchInput) throws ParseException {
		return contactDAO.search(searchInput);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return contactDAO.getTotalRowCount(searchInput);
	}
	
	public void delete(ContactEntity contact) {
		contactDAO.delete(contact);
	}
	
	public void deletePermanently(ContactEntity contact) {
		contactDAO.deletePermanently(contact);
	}
}