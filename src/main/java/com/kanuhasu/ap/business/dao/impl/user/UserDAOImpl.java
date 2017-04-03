package com.kanuhasu.ap.business.dao.impl.user;

import java.text.ParseException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.AbstractDAO;
import com.kanuhasu.ap.business.util.SearchInput;

@Repository
@Transactional
public class UserDAOImpl extends AbstractDAO {
	public boolean makeItAdmin(String emailID, RoleEntity adminRole) {
		UserEntity user;
		Object userObj = this.get(emailID);
		if(userObj != null) {
			user = (UserEntity) userObj;
			user.getRoles().add(adminRole);
			this.update(user);
			return true;
		}
		return false;
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
	public List<UserEntity> search(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(UserEntity.class);
		super.search(searchInput, criteria);
		return criteria.list();
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(UserEntity.class);
		super.getTotalRowCount(searchInput, criteria);
		Long rowCount = (Long) criteria.uniqueResult();
		return rowCount;
	}
}