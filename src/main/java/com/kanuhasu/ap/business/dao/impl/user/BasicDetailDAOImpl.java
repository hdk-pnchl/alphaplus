package com.kanuhasu.ap.business.dao.impl.user;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.BasicDetailEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class BasicDetailDAOImpl extends AbstractDAO{
	
	public UserEntity save(BasicDetailEntity basicDetail, long userID) {
		UserEntity user= null;
		Object userObject = this.getSession().get(UserEntity.class, userID);
		if (userObject != null) {
			user = (UserEntity) userObject;
			basicDetail.setUser(user);
			this.getSession().save(basicDetail);
		}
		return user;
	}
	
	public UserEntity update(BasicDetailEntity basicDetail, long userID) {
		UserEntity user = null;
		Object userObject = this.getSession().get(UserEntity.class, userID);
		if (userObject != null) {
			user = (UserEntity) userObject;
			user.setBasicDetail(basicDetail);
			basicDetail.setUser(user);
			this.getSession().merge(user);
		}
		return user;
	}
	
	public BasicDetailEntity get(long basicDetailID) {
		BasicDetailEntity basicDetail = null;
		Object patientObject = this.getSession().get(BasicDetailEntity.class, basicDetailID);
		if (patientObject != null) {
			basicDetail = (BasicDetailEntity) patientObject;
		}
		return basicDetail;
	}

	@SuppressWarnings("unchecked")
	public List<BasicDetailEntity> list() {
		Criteria criteria = getSession().createCriteria(BasicDetailEntity.class);
		return (List<BasicDetailEntity>) criteria.list();
	}

	public void delete(BasicDetailEntity basicDetail) {
		// TODO Auto-generated method stub
	}

	public void deletePermanently(BasicDetailEntity basicDetail) {
		this.getSession().delete(basicDetail);
	}
}