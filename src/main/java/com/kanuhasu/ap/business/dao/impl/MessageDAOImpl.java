package com.kanuhasu.ap.business.dao.impl;

import java.text.ParseException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.MessageEntity;
import com.kanuhasu.ap.business.util.SearchInput;

@Repository
@Transactional
public class MessageDAOImpl extends AbstractDAO{

	public MessageEntity save(MessageEntity message) {
		this.getSession().save(message);
		return message;
	}

	public MessageEntity update(MessageEntity message) {
		this.getSession().update(message);
		return message;
	}

	public MessageEntity saveOrUpdate(MessageEntity message) {
		this.getSession().saveOrUpdate(message);
		return message;
	}

	public MessageEntity get(long messageID) {
		MessageEntity message = null;
		Object patientObject = this.getSession().get(MessageEntity.class, messageID);
		if (patientObject != null) {
			message = (MessageEntity) patientObject;
		}
		return message;
	}

	@SuppressWarnings("unchecked")
	public List<MessageEntity> list() {
		Criteria criteria = getSession().createCriteria(MessageEntity.class);
		return (List<MessageEntity>) criteria.list();
	}

	public List<MessageEntity> listByEmailID(String emailID) {
		Criteria criteria = getSession().createCriteria(MessageEntity.class);
		if (emailID != null) {
			criteria.add(Restrictions.eq("emailID", emailID));
		}
		@SuppressWarnings("unchecked")
		List<MessageEntity> messageList = criteria.list();
		return messageList;
	}

	@SuppressWarnings("unchecked")
	public List<MessageEntity> search(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(MessageEntity.class);
		super.search(searchInput, criteria);
		return criteria.list();
	}

	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(MessageEntity.class);
		super.getTotalRowCount(searchInput, criteria);
		Long rowCount = (Long) criteria.uniqueResult();
		return rowCount;
	}

	public void delete(MessageEntity message) {
		// TODO Auto-generated method stub
	}

	public void deletePermanently(MessageEntity message) {
		this.getSession().delete(message);
	}
}