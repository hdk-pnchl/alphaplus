package com.nanites.alphaplus.business.dao.impl.user;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nanites.alphaplus.business.bo.user.RoleEntity;
import com.nanites.alphaplus.business.bo.user.UserEntity;
import com.nanites.alphaplus.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class AuthorityDAOImpl extends AbstractDAO {
	
	public RoleEntity save(RoleEntity role) {
		this.getSession().save(role);
		return role;
	}
	
	public RoleEntity update(RoleEntity role) {
		this.getSession().update(role);
		return role;
	}
	
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
	
	public RoleEntity saveOrUpdate(RoleEntity role) {
		this.getSession().saveOrUpdate(role);
		return role;
	}
	
	public RoleEntity get(long roleID) {
		RoleEntity role = null;
		Object roleObject = this.getSession().get(RoleEntity.class, roleID);
		if(roleObject != null) {
			role = (RoleEntity) roleObject;
		}
		return role;
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleEntity> list() {
		Criteria criteria = getSession().createCriteria(RoleEntity.class);
		return (List<RoleEntity>) criteria.list();
	}
	
	public void delete(RoleEntity role) {
		// TODO Auto-generated method stub
	}
	
	public void deletePermanently(RoleEntity role) {
		this.getSession().delete(role);
	}
}