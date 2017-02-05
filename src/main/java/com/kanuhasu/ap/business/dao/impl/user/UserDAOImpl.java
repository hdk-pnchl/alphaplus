package com.kanuhasu.ap.business.dao.impl.user;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.AbstractDAO;
import com.kanuhasu.ap.business.util.SearchInput;

@Repository
@Transactional
public class UserDAOImpl extends AbstractDAO {
	public UserEntity save(UserEntity user) {
		this.getSession().save(user);
		
		user.getBasicDetail().setUser(user);
		user.getAddress().setUser(user);
		
		return user;
	}
	
	public UserEntity update(UserEntity user) {
		this.getSession().update(user);
		return user;
	}
	
	public UserEntity get(long userID) {
		UserEntity userEntity = null;
		Object userObj = this.getSession().get(UserEntity.class, userID);
		if(userObj != null) {
			userEntity = (UserEntity) userObj;
		}
		return userEntity;
	}
	
	public UserEntity get(String emailID) {
		UserEntity user = null;
		Criteria criteria = getSession().createCriteria(UserEntity.class);
		Criteria innerCriteria = criteria.createCriteria("basicDetail");
		if(emailID != null) {
			innerCriteria.add(Restrictions.eq("emailID", emailID));
		}
		@SuppressWarnings("unchecked")
		List<UserEntity> list = criteria.list();
		if(list != null && !list.isEmpty()) {
			user = list.get(0);
		}
		return user;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserEntity> list() {
		Criteria criteria = getSession().createCriteria(UserEntity.class);
		return (List<UserEntity>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserEntity> list(SearchInput searchInput) {
		int beginIndx = (searchInput.getPageNo() * searchInput.getRowsPerPage()) - searchInput.getRowsPerPage();
		Criteria criteria = this.getSession().createCriteria(UserEntity.class);
		criteria.setFirstResult(beginIndx);
		criteria.setMaxResults(searchInput.getRowsPerPage());
		//criteria.addOrder(Order.asc("lastUpdatedOn"));
		return criteria.list();
	}
	
	public Long getTotalRowCount(SearchInput searchInput) {
		Criteria criteria = this.getSession().createCriteria(UserEntity.class);
		criteria.setProjection(Projections.rowCount());
		Long rowCount = (Long) criteria.uniqueResult();
		return rowCount;
	}
	
	public void deletePermanently(UserEntity user) {
		this.getSession().delete(user);
	}
	
	public void delete(UserEntity user) {
		// TODO Auto-generated method stub
	}
}