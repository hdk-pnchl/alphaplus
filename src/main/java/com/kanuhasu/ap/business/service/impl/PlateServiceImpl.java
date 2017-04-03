package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.dao.impl.PlateDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class PlateServiceImpl {
	
	@Autowired
	private PlateDAOImpl plateDAO;
	
	public PlateEntity save(PlateEntity plate) {
		return plateDAO.save(plate);
	}
	
	public PlateEntity update(PlateEntity plate) {
		return plateDAO.update(plate);
	}
	
	public PlateEntity saveOrUpdate(PlateEntity plate) {
		return plateDAO.saveOrUpdate(plate);
	}
	
	public PlateEntity get(long plateId) {
		return plateDAO.get(plateId, PlateEntity.class);
	}
	
	public List<PlateEntity> list() {
		return plateDAO.list(PlateEntity.class);
	}
	
	public List<PlateEntity> search(SearchInput searchInput) throws ParseException {
		return plateDAO.search(searchInput);
	}
	
	public List<PlateEntity> searchByName(String name) {
		return plateDAO.searchByName(name);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return plateDAO.getTotalRowCount(searchInput);
	}
	
	public void delete(PlateEntity plate) {
		plateDAO.delete(plate);
	}
	
	public void deletePermanently(PlateEntity plate) {
		plateDAO.deletePermanently(plate);
	}
}