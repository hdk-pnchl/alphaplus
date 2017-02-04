package com.kanuhasu.ap.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.complaint.ComplaintEntity;
import com.kanuhasu.ap.business.dao.impl.ComplaintDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class ComplaintServiceImpl{
	@Autowired
	private ComplaintDAOImpl basicDetailDAO;
	
	public ComplaintEntity save(ComplaintEntity complaint) {
		this.basicDetailDAO.save(complaint);
		return complaint;
	}
	
	public ComplaintEntity update(ComplaintEntity complaint) {
		this.basicDetailDAO.update(complaint);
		return complaint;
	}
	
	public ComplaintEntity get(long complaintID) {
		return this.basicDetailDAO.get(complaintID);
	}
	
	public List<ComplaintEntity> list() {
		return this.basicDetailDAO.list();
	}

	public List<ComplaintEntity> list(String userEmailID) {
		return this.basicDetailDAO.list();
	}
	
	public List<ComplaintEntity> list(SearchInput searchInput) {
		return this.basicDetailDAO.list(searchInput);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) {
		return this.basicDetailDAO.getTotalRowCount(searchInput);
	}

	public void deletePermanently(ComplaintEntity complaint) {
		this.basicDetailDAO.delete(complaint);
	}
	
	public void delete(ComplaintEntity complaint) {
		// TODO Auto-generated method stub
	}
}