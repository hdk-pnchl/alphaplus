package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobInstEntity;
import com.kanuhasu.ap.business.dao.impl.JobInstDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class JobInstServiceImpl extends AbstractServiceImpl<JobInstEntity> {
	
	@Autowired
	public void setDao(JobInstDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<JobInstEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, JobInstEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, JobInstEntity.class);
	}
}