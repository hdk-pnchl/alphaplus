package com.kanuhasu.ap.business.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.MessageEntity;

@Repository
@Transactional
public class MessageDAOImpl extends AbstractDAO<MessageEntity>{

	public List<MessageEntity> listByEmailID(String emailID) {
		Criteria criteria = super.getSession().createCriteria(MessageEntity.class);
		if (emailID != null) {
			criteria.add(Restrictions.eq("emailID", emailID));
		}
		@SuppressWarnings("unchecked")
		List<MessageEntity> messageList = criteria.list();
		return messageList;
	}
}