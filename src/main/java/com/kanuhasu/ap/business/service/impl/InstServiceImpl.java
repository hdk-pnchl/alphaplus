package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.InstructionEntity;
import com.kanuhasu.ap.business.dao.impl.InstDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class InstServiceImpl extends AbstractServiceImpl<InstructionEntity> {
	
	@Autowired
	public void setDao(InstDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<InstructionEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, InstructionEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, InstructionEntity.class);
	}
}