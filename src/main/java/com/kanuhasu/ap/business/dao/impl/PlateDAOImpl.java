package com.kanuhasu.ap.business.dao.impl;

import java.text.ParseException;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.util.SearchInput;

@Repository
@Transactional
public class PlateDAOImpl extends AbstractDAO {
	@SuppressWarnings("unchecked")
	public List<PlateEntity> searchByName(String name) {
		Criteria criteria = getSession().createCriteria(PlateEntity.class);
		return (List<PlateEntity>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PlateEntity> search(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(PlateEntity.class);
		super.search(searchInput, criteria);
		return criteria.list();
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(PlateEntity.class);
		super.getTotalRowCount(searchInput, criteria);
		Long rowCount = (Long) criteria.uniqueResult();
		return rowCount;
	}
}