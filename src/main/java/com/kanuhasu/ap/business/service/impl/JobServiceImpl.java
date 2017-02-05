package com.kanuhasu.ap.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.dao.impl.JobDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class JobServiceImpl{

	@Autowired
	private JobDAOImpl jobDAO;

	public JobEntity save(JobEntity job) {
		return jobDAO.save(job);
	}

	public JobEntity update(JobEntity job) {
		return jobDAO.update(job);
	}

	public JobEntity saveOrUpdate(JobEntity job) {
		return jobDAO.saveOrUpdate(job);
	}

	public JobEntity get(long jobId) {
		return jobDAO.get(jobId);
	}

	public List<JobEntity> list() {
		return jobDAO.list();
	}

	public List<JobEntity> search(SearchInput searchInput) {
		return jobDAO.search(searchInput);
	}

	public Long getTotalRowCount(SearchInput searchInput) {
		return jobDAO.getTotalRowCount(searchInput);
	}

	public void delete(JobEntity job) {
		jobDAO.delete(job);
	}

	public void deletePermanently(JobEntity job) {
		jobDAO.deletePermanently(job);
	}
}