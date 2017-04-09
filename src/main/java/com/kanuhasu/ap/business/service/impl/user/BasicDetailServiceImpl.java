package com.kanuhasu.ap.business.service.impl.user;

import com.kanuhasu.ap.business.bo.user.BasicDetailEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.BasicDetailDAOImpl;
import com.kanuhasu.ap.business.service.impl.AbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BasicDetailServiceImpl extends AbstractServiceImpl<BasicDetailEntity>{

	@Autowired
	private BasicDetailDAOImpl basicDetailDAO;

	public UserEntity save(BasicDetailEntity basicDetail, long userID) {
		return basicDetailDAO.save(basicDetail, userID);
	}

	public UserEntity update(BasicDetailEntity basicDetail, long userID){
		return basicDetailDAO.update(basicDetail, userID);
	}

	public List<BasicDetailEntity> list() {
		return basicDetailDAO.list(BasicDetailEntity.class);
	}

	public void delete(BasicDetailEntity basicDetail) {
		basicDetailDAO.delete(basicDetail);
	}

	public void deletePermanently(BasicDetailEntity basicDetail) {
		basicDetailDAO.deletePermanently(basicDetail);
	}
}