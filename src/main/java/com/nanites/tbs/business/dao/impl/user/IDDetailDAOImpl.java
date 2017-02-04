package com.nanites.alphaplus.business.dao.impl.user;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nanites.alphaplus.business.bo.user.IDDetailEntity;
import com.nanites.alphaplus.business.bo.user.UserEntity;
import com.nanites.alphaplus.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class IDDetailDAOImpl extends AbstractDAO{
	
	public UserEntity save(IDDetailEntity idDetail, long userID) {
		UserEntity user= null;
		Object userObject = this.getSession().get(UserEntity.class, userID);
		if (userObject != null) {
			user = (UserEntity) userObject;
			idDetail.setUser(user);
			this.getSession().save(idDetail);
		}
		return user;
	}
	
	public UserEntity update(IDDetailEntity idDetail, long userID) {
		UserEntity user = null;
		Object userObject = this.getSession().get(UserEntity.class, userID);
		if (userObject != null) {
			user = (UserEntity) userObject;
			user.setIdDetail(idDetail);
			idDetail.setUser(user);
			this.getSession().merge(user);
		}
		return user;
	}
	
	public IDDetailEntity get(long addressID) {
		IDDetailEntity IDDetail = null;
		Object patientObject = this.getSession().get(IDDetailEntity.class, addressID);
		if (patientObject != null) {
			IDDetail = (IDDetailEntity) patientObject;
		}
		return IDDetail;
	}

	@SuppressWarnings("unchecked")
	public List<IDDetailEntity> list() {
		Criteria criteria = getSession().createCriteria(IDDetailEntity.class);
		return (List<IDDetailEntity>) criteria.list();
	}

	public void delete(IDDetailEntity IDDetail) {
		// TODO Auto-generated method stub
	}

	public void deletePermanently(IDDetailEntity IDDetail) {
		this.getSession().delete(IDDetail);
	}
}