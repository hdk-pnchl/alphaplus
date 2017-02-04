package com.nanites.alphaplus.business.dao.impl.user;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nanites.alphaplus.business.bo.user.EducationEntity;
import com.nanites.alphaplus.business.bo.user.UserEntity;
import com.nanites.alphaplus.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class EducationDAOImpl extends AbstractDAO{
	
	public UserEntity save(EducationEntity education, long userID) {
		UserEntity user= null;
		Object userObject = this.getSession().get(UserEntity.class, userID);
		if (userObject != null) {
			user = (UserEntity) userObject;
			education.setUser(user);
			this.getSession().save(education);
		}
		return user;
	}
	
	public UserEntity update(EducationEntity education, long userID) {
		UserEntity user = null;
		Object userObject = this.getSession().get(UserEntity.class, userID);
		if (userObject != null) {
			user = (UserEntity) userObject;
			user.setEducation(education);
			education.setUser(user);
			this.getSession().merge(user);
		}
		return user;
	}
	
	public EducationEntity get(long addressID) {
		EducationEntity education = null;
		Object patientObject = this.getSession().get(EducationEntity.class, addressID);
		if (patientObject != null) {
			education = (EducationEntity) patientObject;
		}
		return education;
	}

	@SuppressWarnings("unchecked")
	public List<EducationEntity> list() {
		Criteria criteria = getSession().createCriteria(EducationEntity.class);
		return (List<EducationEntity>) criteria.list();
	}

	public void delete(EducationEntity education) {
		// TODO Auto-generated method stub
	}

	public void deletePermanently(EducationEntity education) {
		this.getSession().delete(education);
	}
}