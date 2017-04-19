package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.dao.impl.JobDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class JobServiceImpl extends AbstractServiceImpl<JobEntity>{

	@Autowired
	public void setDao(JobDAOImpl dao) {
		this.dao = dao;
	}
	
	public List<JobEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, JobEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, JobEntity.class);
	}
}