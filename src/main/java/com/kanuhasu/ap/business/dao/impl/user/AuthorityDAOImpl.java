package com.kanuhasu.ap.business.dao.impl.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class AuthorityDAOImpl extends AbstractDAO<RoleEntity> {
	
	@Autowired
	private UserDAOImpl userDao;

	public UserEntity addRoleToUser(RoleEntity role, long userID) {
		UserEntity user = null;
		Object userObject = userDao.fetchByID(userID);
		if(userObject != null) {
			user = (UserEntity) userObject;
			user.getRoles().add(role);
			super.getSession().merge(user);
		}
		return user;
	}
}