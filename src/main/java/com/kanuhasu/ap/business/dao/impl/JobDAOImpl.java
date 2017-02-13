package com.kanuhasu.ap.business.dao.impl;

import java.text.ParseException;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.util.SearchInput;

@Repository
@Transactional
public class JobDAOImpl extends AbstractDAO {
	
	public JobEntity save(JobEntity job) {
		this.getSession().save(job);
		return job;
	}
	
	public JobEntity update(JobEntity job) {
		this.getSession().update(job);
		return job;
	}
	
	public JobEntity saveOrUpdate(JobEntity job) {
		this.getSession().saveOrUpdate(job);
		return job;
	}
	
	public JobEntity get(long jobID) {
		JobEntity job = null;
		Object jobObject = this.getSession().get(JobEntity.class, jobID);
		if(jobObject != null) {
			job = (JobEntity) jobObject;
		}
		return job;
	}
	
	@SuppressWarnings("unchecked")
	public List<JobEntity> list() {
		Criteria criteria = getSession().createCriteria(JobEntity.class);
		return (List<JobEntity>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<JobEntity> search(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(JobEntity.class);
		super.search(searchInput, criteria);
		return criteria.list();
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(JobEntity.class);
		super.getTotalRowCount(searchInput, criteria);
		Long rowCount = (Long) criteria.uniqueResult();
		return rowCount;
	}
	
	public void delete(JobEntity job) {
		// TODO Auto-generated method stub
	}
	
	public void deletePermanently(JobEntity job) {
		this.getSession().delete(job);
	}
}