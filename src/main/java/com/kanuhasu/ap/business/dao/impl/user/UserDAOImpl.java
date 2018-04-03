package com.kanuhasu.ap.business.dao.impl.user;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class UserDAOImpl extends AbstractDAO<UserEntity> {

	public UserEntity update(UserEntity entity) {
		entity = super.save(entity);
		this.initLazyProp(entity);
		return entity;
	}

	public UserEntity fetchByEmailID(String emailID) {
		UserEntity entity = null;
		if (emailID != null) {
			Criteria criteria = super.getSession().createCriteria(UserEntity.class);
			criteria.add(Restrictions.eq("emailID", emailID));
			@SuppressWarnings("unchecked")
			List<UserEntity> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				entity = list.get(0);
				this.initLazyProp(entity);
			}
		}
		return entity;
	}

	public UserEntity fetchByID(long id) {
		UserEntity user = super.fetchByID(id, UserEntity.class);
		if (user != null) {
			this.initLazyProp(user);
		}
		return user;
	}

	public UserEntity fetchByName(String name) {
		UserEntity entity = null;
		Criteria criteria = this.getSession().createCriteria(UserEntity.class);
		criteria.add(Restrictions.eq("name", name));
		@SuppressWarnings("unchecked")
		List<UserEntity> users = criteria.list();
		if (!users.isEmpty()) {
			entity = users.get(0);
			this.initLazyProp(entity);
		}
		return entity;
	}

	public List<UserEntity> searchByName(String name) {
		Criteria criteria = this.getSession().createCriteria(UserEntity.class);
		criteria.add(Restrictions.like("name", "%" + name + "%"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<UserEntity> entities = criteria.list();
		return entities;
	}

	public void initLazyProp(UserEntity user) {
		Hibernate.initialize(user.getAddresses());
		Hibernate.initialize(user.getContacts());
		Hibernate.initialize(user.getRoles());
	}

	public Response makeItAdmin(String emailID, RoleEntity adminRole) {
		if (StringUtils.isEmpty(emailID)) {
			emailID = "hdk.pnchl@gmail.com";
		}
		UserEntity user = this.fetchByEmailID(emailID);
		if (user == null) {
			user = new UserEntity();
			user.setEmailID(emailID);
			user.setName("Hardik P");
			user.setPassword("1");
		}
		user.getRoles().add(adminRole);
		user = this.save(user);
		return Response.Success(user);
	}
}