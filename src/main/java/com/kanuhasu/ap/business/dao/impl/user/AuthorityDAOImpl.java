package com.kanuhasu.ap.business.dao.impl.user;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class AuthorityDAOImpl extends AbstractDAO {
	
	public UserEntity addRoleToUser(RoleEntity role, long userID) {
		UserEntity user = null;
		Object userObject = this.getSession().get(UserEntity.class, userID);
		if(userObject != null) {
			user = (UserEntity) userObject;
			user.getRoles().add(role);
			this.getSession().merge(user);
		}
		return user;
	}
}