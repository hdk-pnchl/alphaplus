package com.kanuhasu.ap.business.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.ClientEntity;

@Repository
@Transactional
public class ClientDAOImpl extends AbstractDAO<ClientEntity> {

	public ClientEntity update(ClientEntity entity) {
		entity = super.save(entity);
		this.initLazyProp(entity);
		return entity;
	}

	public ClientEntity fetchByEmailID(String emailID) {
		ClientEntity entity = null;
		if (emailID != null) {
			Criteria criteria = super.getSession().createCriteria(ClientEntity.class);
			criteria.add(Restrictions.eq("emailID", emailID));
			@SuppressWarnings("unchecked")
			List<ClientEntity> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				entity = list.get(0);
				this.initLazyProp(entity);
			}
		}
		return entity;
	}

	public ClientEntity fetchByID(long id) {
		ClientEntity entity = super.fetchByID(id, ClientEntity.class);
		this.initLazyProp(entity);
		return entity;
	}

	public ClientEntity fetchByName(String name) {
		ClientEntity entity = null;
		Criteria criteria = this.getSession().createCriteria(ClientEntity.class);
		criteria.add(Restrictions.eq("name", name));
		@SuppressWarnings("unchecked")
		List<ClientEntity> users = criteria.list();
		if (!users.isEmpty()) {
			entity = users.get(0);
			this.initLazyProp(entity);
		}
		return entity;
	}

	public Map<Long, ClientEntity> searchById(Set<Long> clientIds) {
		Criteria criteria = this.getSession().createCriteria(ClientEntity.class);
		criteria.add(Restrictions.in("id", clientIds));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<ClientEntity> clients = criteria.list();
		Map<Long, ClientEntity> clientMap = new HashMap<>();
		for (ClientEntity client : clients) {
			clientMap.put(client.getId(), client);
		}
		return clientMap;
	}

	public List<ClientEntity> searchByName(String name) {
		Criteria criteria = this.getSession().createCriteria(ClientEntity.class);
		criteria.add(Restrictions.like("name", "%" + name + "%"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<ClientEntity> entities = criteria.list();
		return entities;
	}

	public void initLazyProp(ClientEntity client) {
		Hibernate.initialize(client.getAddresses());
		Hibernate.initialize(client.getContacts());
	}
}