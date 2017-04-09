package com.kanuhasu.ap.business.service.impl;

import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.dao.impl.JobDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobServiceImpl extends AbstractServiceImpl<JobEntity>{

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
		return jobDAO.get(jobId, JobEntity.class);
	}

	public List<JobEntity> list() {
		return jobDAO.list(JobEntity.class);
	}

	public void delete(JobEntity job) {
		jobDAO.delete(job);
	}

	public void deletePermanently(JobEntity job) {
		jobDAO.deletePermanently(job);
	}
}