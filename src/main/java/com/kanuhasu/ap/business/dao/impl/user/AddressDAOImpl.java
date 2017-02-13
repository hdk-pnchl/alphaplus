package com.kanuhasu.ap.business.dao.impl.user;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class AddressDAOImpl extends AbstractDAO{
	public AddressEntity save(AddressEntity address) {
		this.getSession().save(address);
		return address;
	}

	public AddressEntity update(AddressEntity address) {
		this.getSession().merge(address);
		return address;
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