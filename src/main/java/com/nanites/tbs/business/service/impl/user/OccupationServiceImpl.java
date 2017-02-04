package com.nanites.alphaplus.business.service.impl.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nanites.alphaplus.business.bo.user.OccupationEntity;
import com.nanites.alphaplus.business.bo.user.UserEntity;
import com.nanites.alphaplus.business.dao.impl.user.OccupationDAOImpl;

@Service
@Transactional
public class OccupationServiceImpl{

	@Autowired
	private OccupationDAOImpl occupationDAO;

	public UserEntity save(OccupationEntity occupation, long userID) {
		return occupationDAO.save(occupation, userID);
	}

	public UserEntity update(OccupationEntity occupation, long userID){
		return occupationDAO.update(occupation, userID);
	}

	public OccupationEntity get(long addressId) {
		return occupationDAO.get(addressId);
	}

	public List<OccupationEntity> list() {
		return occupationDAO.list();
	}

	public void delete(OccupationEntity occupation) {
		occupationDAO.delete(occupation);
	}

	public void deletePermanently(OccupationEntity occupation) {
		occupationDAO.deletePermanently(occupation);
	}
}