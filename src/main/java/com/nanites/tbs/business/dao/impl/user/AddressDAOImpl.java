package com.nanites.alphaplus.business.dao.impl.user;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nanites.alphaplus.business.bo.user.AddressEntity;
import com.nanites.alphaplus.business.bo.user.UserEntity;
import com.nanites.alphaplus.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class AddressDAOImpl extends AbstractDAO{
	public UserEntity save(AddressEntity address, long userID) {
		UserEntity user= null;
		Object userObject = this.getSession().get(UserEntity.class, userID);
		if (userObject != null) {
			user = (UserEntity) userObject;
			address.setUser(user);
			this.getSession().save(address);
		}
		return user;
	}

	public UserEntity update(AddressEntity address, long userID) {
		UserEntity user = null;
		Object userObject = this.getSession().get(UserEntity.class, userID);
		if (userObject != null) {
			user = (UserEntity) userObject;
			user.setAddress(address);
			address.setUser(user);
			this.getSession().merge(user);
		}
		return user;
	}
	
	public AddressEntity get(long addressID) {
		AddressEntity address = null;
		Object patientObject = this.getSession().get(AddressEntity.class, addressID);
		if (patientObject != null) {
			address = (AddressEntity) patientObject;
		}
		return address;
	}

	@SuppressWarnings("unchecked")
	public List<AddressEntity> list() {
		Criteria criteria = getSession().createCriteria(AddressEntity.class);
		return (List<AddressEntity>) criteria.list();
	}

	public void delete(AddressEntity address) {
		// TODO Auto-generated method stub
	}

	public void deletePermanently(AddressEntity address) {
		this.getSession().delete(address);
	}
}