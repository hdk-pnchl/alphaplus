package com.nanites.alphaplus.business.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nanites.alphaplus.business.bo.complaint.ComplaintEntity;
import com.nanites.alphaplus.business.util.SearchInput;

@Repository
@Transactional
public class ComplaintDAOImpl extends AbstractDAO {
	public ComplaintEntity save(ComplaintEntity complaint) {
		this.getSession().save(complaint);
		return complaint;
	}
	
	public ComplaintEntity update(ComplaintEntity complaint) {
		this.getSession().update(complaint);
		return complaint;
	}
	
	public ComplaintEntity get(long complaintID) {
		ComplaintEntity complaint = null;
		Object complaintObj = this.getSession().get(ComplaintEntity.class, complaintID);
		if(complaintObj != null) {
			complaint = (ComplaintEntity) complaintObj;
		}
		return complaint;
	}
	
	@SuppressWarnings("unchecked")
	public List<ComplaintEntity> list() {
		Criteria criteria = getSession().createCriteria(ComplaintEntity.class);
		return (List<ComplaintEntity>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ComplaintEntity> list(long userID) {
		Criteria criteria = getSession().createCriteria(ComplaintEntity.class);
		return (List<ComplaintEntity>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ComplaintEntity> list(SearchInput searchInput) {
		int beginIndx = (searchInput.getPageNo() * searchInput.getRowsPerPage()) - searchInput.getRowsPerPage();
		Criteria criteria = this.getSession().createCriteria(ComplaintEntity.class);
		criteria.setFirstResult(beginIndx);
		criteria.setMaxResults(searchInput.getRowsPerPage());
		//criteria.addOrder(Order.asc("lastUpdatedOn"));
		return criteria.list();
	}
	
	public Long getTotalRowCount(SearchInput searchInput) {
		Criteria criteria = this.getSession().createCriteria(ComplaintEntity.class);
		criteria.setProjection(Projections.rowCount());
		Long rowCount = (Long) criteria.uniqueResult();
		return rowCount;
	}
	
	public void deletePermanently(ComplaintEntity complaint) {
		this.getSession().delete(complaint);
	}
	
	public void delete(ComplaintEntity complaint) {
		// TODO Auto-generated method stub
	}
}