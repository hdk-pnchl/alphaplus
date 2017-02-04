package com.nanites.alphaplus.business.dao.impl.user;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nanites.alphaplus.business.bo.user.OccupationEntity;
import com.nanites.alphaplus.business.bo.user.UserEntity;
import com.nanites.alphaplus.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class OccupationDAOImpl extends AbstractDAO{
	public UserEntity save(OccupationEntity occupation, long userID) {
		UserEntity user= null;
		Object userObject = this.getSession().get(UserEntity.class, userID);
		if (userObject != null) {
			user = (UserEntity) userObject;
			occupation.setUser(user);
			this.getSession().save(occupation);
		}
		return user;
	}
	
	public UserEntity update(OccupationEntity occupation, long userID) {
		UserEntity user = null;
		Object userObject = this.getSession().get(UserEntity.class, userID);
		if (userObject != null) {
			user = (UserEntity) userObject;
			user.setOccupation(occupation);
			occupation.setUser(user);
			this.getSession().merge(user);
		}
		return user;
	}
	
	public OccupationEntity get(long addressID) {
		OccupationEntity occupation = null;
		Object patientObject = this.getSession().get(OccupationEntity.class, addressID);
		if (patientObject != null) {
			occupation = (OccupationEntity) patientObject;
		}
		return occupation;
	}

	@SuppressWarnings("unchecked")
	public List<OccupationEntity> list() {
		Criteria criteria = getSession().createCriteria(OccupationEntity.class);
		return (List<OccupationEntity>) criteria.list();
	}

	public void delete(OccupationEntity occupation) {
		// TODO Auto-generated method stub
	}

	public void deletePermanently(OccupationEntity occupation) {
		this.getSession().delete(occupation);
	}
}