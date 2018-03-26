package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.dao.impl.AbstractDAO;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public abstract class AbstractServiceImpl<E> {
	protected AbstractDAO<E> dao;
	
	public E save(E e) {
		return dao.save(e);
	}
	
	public E merge(E e) {
		return dao.merge(e);
	}
	
	public E saveOrUpdate(E e) {
		return dao.saveOrUpdate(e);
	}
	
	public E get(long clientId, Class<E> type) {
		return dao.get(clientId, type);
	}
	
	public List<E> list(Class<E> type) {
		return dao.list(type);
	}
	
	public List<E> search(SearchInput searchInput, Class<E> clazz) throws ParseException {
		return dao.search(searchInput, clazz);
	}
		
	public Long getTotalRowCount(SearchInput searchInput, Class<E> clazz) throws ParseException {
		return dao.getTotalRowCount(searchInput, clazz);
	}
	
	public void delete(E e) {
		dao.delete(e);
	}
	
	public void deletePermanently(E e) {
		dao.deletePermanently(e);
	}
}