package com.kanuhasu.ap.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.MessageEntity;
import com.kanuhasu.ap.business.dao.impl.MessageDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class MessageServiceImpl{

	@Autowired
	MessageDAOImpl messageDAO;

	public MessageEntity save(MessageEntity message) {
		return messageDAO.save(message);
	}

	public MessageEntity update(MessageEntity message) {
		return messageDAO.update(message);
	}

	public MessageEntity saveOrUpdate(MessageEntity message) {
		return messageDAO.saveOrUpdate(message);
	}

	public MessageEntity get(long messageId) {
		return messageDAO.get(messageId);
	}

	public List<MessageEntity> list() {
		return messageDAO.list();
	}

	public List<MessageEntity> search(SearchInput searchInput) {
		return messageDAO.search(searchInput);
	}

	public Long getTotalRowCount(SearchInput searchInput) {
		return messageDAO.getTotalRowCount(searchInput);
	}

	public void delete(MessageEntity message) {
		messageDAO.delete(message);
	}

	public void deletePermanently(MessageEntity message) {
		messageDAO.deletePermanently(message);
	}

	public List<MessageEntity> listByEmailID(String emailId) {
		return messageDAO.listByEmailID(emailId);
	}
}