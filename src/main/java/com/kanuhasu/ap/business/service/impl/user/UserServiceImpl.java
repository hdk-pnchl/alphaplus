package com.kanuhasu.ap.business.service.impl.user;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.service.impl.AbstractServiceImpl;
import com.kanuhasu.ap.business.type.bo.user.Roles;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class UserServiceImpl extends AbstractServiceImpl<UserEntity>{
	@Autowired
	private AuthorityServiceImpl authorityService;
	
	@Autowired
	public void setDao(UserDAOImpl dao) {
		this.dao = dao;
	}
	
	@Override
	public UserEntity save(UserEntity user) {
		RoleEntity role = authorityService.getAuthorityMap().get(Roles.MEMBER);
		user.getRoles().add(role);
		super.save(user);
		return user;
	}
	
	@Override
	public UserEntity saveOrUpdate(UserEntity user) {
		/*
		UserEntity loggedInUser= this.getByEmailID(CommonUtil.fetchLoginID());
		user.setLastUpdatedBy(loggedInUser);
		user.setLastUpdatedOn(new Date());
		*/
		return dao.saveOrUpdate(user);
	}
	
	public UserEntity searchByName(String name) {
		return ((UserDAOImpl)dao).searchByName(name);
	}
	
	public List<UserEntity> getAllByName(String name) {
		return ((UserDAOImpl)dao).getAllByName(name);
	}
	
	public boolean makeItAdmin(String emailID) {
		RoleEntity adminRole = authorityService.getAuthorityMap().get(Roles.ADMIN);
		return ((UserDAOImpl)dao).makeItAdmin(emailID, adminRole);
	}
	
	public UserEntity getByEmailID(String emailID) {
		return ((UserDAOImpl)dao).getByEmailID(emailID);
	}

	public List<UserEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, UserEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, UserEntity.class);
	}
}