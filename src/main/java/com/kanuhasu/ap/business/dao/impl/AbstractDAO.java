package com.kanuhasu.ap.business.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.SearchInput;

public abstract class AbstractDAO {
	private DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
	
	@Autowired
	private SessionFactory sessionFactory;
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	protected void search(SearchInput searchInput, Criteria criteria) throws ParseException {
		int beginIndx = (searchInput.getPageNo() * searchInput.getRowsPerPage()) - searchInput.getRowsPerPage();
		if(searchInput.getSearchData() != null) {
			for (Map<String, String> entry : searchInput.getSearchData()) {
				if(StringUtils.isNotEmpty(entry.get(Param.VALUE.val()))) {
					if(StringUtils.isNotEmpty(entry.get(Param.VALUE_TYPE.val())) && entry.get(Param.VALUE_TYPE.val()).equals(Param.Type.STRING.val())) {
						criteria.add(Restrictions.like(entry.get(Param.NAME.val()), "%" + entry.get(Param.VALUE.val()) + "%"));
					}
					else if(StringUtils.isNotEmpty(entry.get(Param.VALUE_TYPE.val())) && entry.get(Param.VALUE_TYPE.val()).equals(Param.Type.DATE.val())) {
						Date date = df.parse(entry.get(Param.VALUE.val()));
						criteria.add(Restrictions.eq(entry.get(Param.NAME.val()), date));
					}
				}
			}
		}
		criteria.setFirstResult(beginIndx);
		criteria.setMaxResults(searchInput.getRowsPerPage());
		criteria.addOrder(Order.desc("date"));
	}
	
	public void getTotalRowCount(SearchInput searchInput, Criteria criteria) throws ParseException {
		if(searchInput.getSearchData() != null) {
			for (Map<String, String> entry : searchInput.getSearchData()) {
				if(StringUtils.isNotEmpty(entry.get(Param.VALUE.val()))) {
					if(StringUtils.isNotEmpty(entry.get(Param.VALUE_TYPE.val())) && entry.get(Param.VALUE_TYPE.val()).equals(Param.Type.STRING.val())) {
						criteria.add(Restrictions.like(entry.get(Param.NAME.val()), "%" + entry.get(Param.VALUE.val()) + "%"));
					}
					else if(StringUtils.isNotEmpty(entry.get(Param.VALUE_TYPE.val())) && entry.get(Param.VALUE_TYPE.val()).equals(Param.Type.DATE.val())) {
						Date date = df.parse(entry.get(Param.VALUE.val()));
						criteria.add(Restrictions.eq(entry.get(Param.NAME.val()), date));
					}
				}
			}
		}
		criteria.setProjection(Projections.rowCount());
	}
}
