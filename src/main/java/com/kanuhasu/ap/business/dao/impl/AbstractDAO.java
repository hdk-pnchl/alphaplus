package com.kanuhasu.ap.business.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;

@Repository
@Transactional
public abstract class AbstractDAO<E> {
	
	private DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
	
	@Autowired
	private SessionFactory sessionFactory;
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
		/*
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			return sessionFactory.openSession();
		}
		*/		
	}
	
	public E save(E object) {
		this.getSession().save(object);
		return object;
	}
	
	public E update(E object) {
		this.getSession().merge(object);
		return object;
	}
	
	public E saveOrUpdate(E object) {
		this.getSession().saveOrUpdate(object);
		return object;
	}
	
	@SuppressWarnings("unchecked")
	public E get(long id, Class<E> type) {
		E entity = null;
		Object obj = this.getSession().get(type, id);
		if(obj != null) {
			entity = (E) obj;
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public List<E> list(Class<E> type) {
		Criteria criteria = getSession().createCriteria(type);
		return (List<E>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public E saveOrUpdate(Class<E> clazz) {
		this.getSession().saveOrUpdate(clazz);
		return (E) clazz;
	}
	
	@SuppressWarnings("unchecked")
	public List<E> search(SearchInput searchInput, Class<E> clazz) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(clazz);	
		int beginIndx = (searchInput.getPageNo() * searchInput.getRowsPerPage()) - searchInput.getRowsPerPage();
		if(searchInput.getSearchData() != null) {
			for (Map<String, String> entry : searchInput.getSearchData()) {
				if(StringUtils.isNotEmpty(entry.get(Param.value.name()))) {
					if(StringUtils.isNotEmpty(entry.get(Param.type.name())) && entry.get(Param.type.name()).equals(Param.DataType.string.name())) {
						criteria.add(Restrictions.like(entry.get(Param.name.name()), "%" + entry.get(Param.value.name()) + "%"));
					}
					else if(StringUtils.isNotEmpty(entry.get(Param.type.name())) && entry.get(Param.type.name()).equals(Param.DataType.date.name())) {
						String ipDateStr= entry.get(Param.value.name());
						Response dateProcessResp= CommonUtil.processDate(ipDateStr);
						boolean isDateAvailable= Boolean.parseBoolean((String) dateProcessResp.getParam(Param.DataType.DateTime.DATE_AVAILABLE.name()));
						boolean isFullDateAvailable= Boolean.parseBoolean((String) dateProcessResp.getParam(Param.DataType.DateTime.FULL_DATE_AVAILABLE.name()));

						Date ipDate = df.parse((String) dateProcessResp.getParam(Param.DATA.name()));
						if(isFullDateAvailable){
							criteria.add(Restrictions.eq(entry.get(Param.name.name()), ipDate));
						}else if(isDateAvailable){
							Date endDate = DateUtils.addDays(ipDate, 1);
							criteria.add(Restrictions.ge(entry.get(Param.name.name()), ipDate));
							criteria.add(Restrictions.le(entry.get(Param.name.name()), endDate));
						}else{
							criteria.add(Restrictions.ge(entry.get(Param.name.name()), ipDate));
						}
					}
				}
			}
		}
		criteria.setFirstResult(beginIndx);
		criteria.setMaxResults(searchInput.getRowsPerPage());
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();				
	}
	
	public long getTotalRowCount(SearchInput searchInput, Class<E> clazz) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(clazz);			
		if(searchInput.getSearchData() != null) {
			for (Map<String, String> entry : searchInput.getSearchData()) {
				if(StringUtils.isNotEmpty(entry.get(Param.value.name()))) {
					if(StringUtils.isNotEmpty(entry.get(Param.type.name())) && entry.get(Param.type.name()).equals(Param.DataType.string.name())) {
						criteria.add(Restrictions.like(entry.get(Param.name.name()), "%" + entry.get(Param.value.name()) + "%"));
					}
					else if(StringUtils.isNotEmpty(entry.get(Param.type.name())) && entry.get(Param.type.name()).equals(Param.DataType.date.name())) {
						String ipDateStr= entry.get(Param.value.name());
						Response dateProcessResp= CommonUtil.processDate(ipDateStr);
						boolean isDateAvailable= Boolean.parseBoolean((String) dateProcessResp.getParam(Param.DataType.DateTime.DATE_AVAILABLE.name()));
						boolean isFullDateAvailable= Boolean.parseBoolean((String) dateProcessResp.getParam(Param.DataType.DateTime.FULL_DATE_AVAILABLE.name()));

						Date ipDate = df.parse((String) dateProcessResp.getParam(Param.DATA.name()));
						if(isFullDateAvailable){
							criteria.add(Restrictions.eq(entry.get(Param.name.name()), ipDate));
						}else if(isDateAvailable){
							Date endDate = DateUtils.addDays(ipDate, 1);
							criteria.add(Restrictions.ge(entry.get(Param.name.name()), ipDate));
							criteria.add(Restrictions.le(entry.get(Param.name.name()), endDate));
						}else{
							criteria.add(Restrictions.ge(entry.get(Param.name.name()), ipDate));
						}
					}
				}
			}
		}
		criteria.setProjection(Projections.rowCount());
		Long rowCount = (Long) criteria.uniqueResult();
		return rowCount;			
	}
	
	public void delete(E entity) {
	}
	
	public void deletePermanently(E entity) {
		this.getSession().delete(entity);
	}	
}