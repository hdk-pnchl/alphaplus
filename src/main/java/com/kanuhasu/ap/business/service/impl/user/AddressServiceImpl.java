package com.kanuhasu.ap.business.service.impl.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.AddressDAOImpl;

@Service
@Transactional
public class AddressServiceImpl{

	@Autowired
	private AddressDAOImpl addressDAO;

	public UserEntity save(AddressEntity address, long userID) {
		return addressDAO.save(address, userID);
	}

	public UserEntity update(AddressEntity address, long userID){
		return addressDAO.update(address, userID);
	}

	public AddressEntity get(long addressId) {
		return addressDAO.get(addressId);
	}

	public List<AddressEntity> list() {
		return addressDAO.list();
	}

	public void delete(AddressEntity address) {
		addressDAO.delete(address);
	}

	public void deletePermanently(AddressEntity address) {
		addressDAO.deletePermanently(address);
	}
}