package com.kanuhasu.ap.business.dao.impl.user;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.AbstractDAO;
import com.kanuhasu.ap.business.util.CommonUtil;

@Repository
@Transactional
public class UserDAOImpl extends AbstractDAO<UserEntity> {

	@Autowired
	private AddressDAOImpl addressDAOImpl;
	@Autowired
	private ContactDAOImpl contactDAOImpl;

	public boolean makeItAdmin(String emailID, RoleEntity adminRole) {
		UserEntity user;
		Object userObj = this.getByEmailID(emailID);
		if (userObj != null) {
			user = (UserEntity) userObj;
			user.getRoles().add(adminRole);
			this.update(user);
			return true;
		}
		return false;
	}

	public UserEntity getByEmailID(String emailID) {
		UserEntity user = null;
		if(emailID != null) {
			Criteria criteria = super.getSession().createCriteria(UserEntity.class);
			criteria.add(Restrictions.eq("emailID", emailID));
			@SuppressWarnings("unchecked")
			List<UserEntity> list = criteria.list();
			if (list != null && !list.isEmpty()) {
				user = list.get(0);
			}
		}
		return user;
	}

	@Override
	public UserEntity saveOrUpdate(UserEntity user) {
		UserEntity loggedInUser;
		String loggedInUserEmailID = CommonUtil.fetchLoginID();
		if (loggedInUserEmailID.equals(user.getEmailID())) {
			loggedInUser = user;
		} else {
			loggedInUser = this.getByEmailID(loggedInUserEmailID);
		}
		user.setLastUpdatedBy(loggedInUser);
		user.setLastUpdatedOn(new Date());

		if (user.getAddresses() != null) {
			for (AddressEntity address : user.getAddresses()) {
				if (address != null) {
					address.setLastUpdatedBy(loggedInUser);
					address.setLastUpdatedOn(new Date());
					addressDAOImpl.saveOrUpdate(address);
				}
			}
		}
		if (user.getContacts() != null) {
			for (ContactEntity contact : user.getContacts()) {
				if (contact != null) {
					contact.setLastUpdatedBy(loggedInUser);
					contact.setLastUpdatedOn(new Date());
					contactDAOImpl.saveOrUpdate(contact);
				}
			}
		}
		super.saveOrUpdate(user);
		return user;
	}

	@SuppressWarnings("unchecked")
	public UserEntity searchByName(String name) {
		UserEntity user = null;
		Criteria criteria = this.getSession().createCriteria(UserEntity.class);
		criteria.add(Restrictions.eq("name", name));
		List<UserEntity> users = criteria.list();
		if (!users.isEmpty()) {
			user = users.get(0);
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> getAllByName(String name) {
		Criteria criteria = this.getSession().createCriteria(UserEntity.class);
		criteria.add(Restrictions.like("name", "%" + name + "%"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<UserEntity> users = criteria.list();
		return users;
	}

	public UserEntity get(long id) {
		return super.get(id, UserEntity.class);
	}
}