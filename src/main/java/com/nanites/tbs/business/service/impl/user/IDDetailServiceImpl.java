package com.nanites.alphaplus.business.service.impl.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nanites.alphaplus.business.bo.user.IDDetailEntity;
import com.nanites.alphaplus.business.bo.user.UserEntity;
import com.nanites.alphaplus.business.dao.impl.user.IDDetailDAOImpl;

@Service
@Transactional
public class IDDetailServiceImpl{

	@Autowired
	private IDDetailDAOImpl idDetailDAO;

	public UserEntity save(IDDetailEntity IDDetail, long userID) {
		return idDetailDAO.save(IDDetail, userID);
	}

	public UserEntity update(IDDetailEntity IDDetail, long userID){
		return idDetailDAO.update(IDDetail, userID);
	}

	public IDDetailEntity get(long addressId) {
		return idDetailDAO.get(addressId);
	}

	public List<IDDetailEntity> list() {
		return idDetailDAO.list();
	}

	public void delete(IDDetailEntity IDDetail) {
		idDetailDAO.delete(IDDetail);
	}

	public void deletePermanently(IDDetailEntity IDDetail) {
		idDetailDAO.deletePermanently(IDDetail);
	}
}