package com.nanites.alphaplus.business.service.impl.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nanites.alphaplus.business.bo.user.EducationEntity;
import com.nanites.alphaplus.business.bo.user.UserEntity;
import com.nanites.alphaplus.business.dao.impl.user.EducationDAOImpl;

@Service
@Transactional
public class EducationServiceImpl{

	@Autowired
	private EducationDAOImpl educationDAO;

	public UserEntity save(EducationEntity education, long userID) {
		return educationDAO.save(education, userID);
	}

	public UserEntity update(EducationEntity education, long userID){
		return educationDAO.update(education, userID);
	}

	public EducationEntity get(long addressId) {
		return educationDAO.get(addressId);
	}

	public List<EducationEntity> list() {
		return educationDAO.list();
	}

	public void delete(EducationEntity education) {
		educationDAO.delete(education);
	}

	public void deletePermanently(EducationEntity education) {
		educationDAO.deletePermanently(education);
	}
}