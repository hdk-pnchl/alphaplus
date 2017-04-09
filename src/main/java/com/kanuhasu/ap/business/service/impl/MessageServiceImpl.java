package com.kanuhasu.ap.business.service.impl;

import com.kanuhasu.ap.business.bo.MessageEntity;
import com.kanuhasu.ap.business.dao.impl.MessageDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageServiceImpl extends AbstractServiceImpl<MessageEntity>{

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
		return messageDAO.get(messageId, MessageEntity.class);
	}

	public List<MessageEntity> list() {
		return messageDAO.list(MessageEntity.class);
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